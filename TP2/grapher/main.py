import sys

import pandas as pd
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.modifiers import CreateBondsModifier
from ovito.pipeline import StaticSource, Pipeline


def export_to_ovito(static_file, dynamic_file):
    data = DataCollection()
    particle_data = get_particle_data(static_file, dynamic_file)

    def simulation_cell(frame, data):
        # Insert a new SimulationCell object into a data collection:
        cell = SimulationCell(pbc=(False, False, False))
        cell[:, 0] = (4, 0, 0)
        cell[:, 1] = (0, 2, 0)
        cell[:, 2] = (0, 0, 2)
        data.objects.append(cell)

        particles = get_particles([frame])
        data.objects.append(particles)

    # Create a Particles object containing two particles:
    particles = Particles()
    particles.create_property('Position', data=[[0, 0, 0], [2, 0, 0]])
    data.objects.append(particles)

    # Create a new Pipeline with a StaticSource as data source:
    pipeline = Pipeline(source=StaticSource(data=data))

    # Apply a modifier:
    pipeline.modifiers.append(CreateBondsModifier(cutoff=3.0))

    export_file(pipeline, "grapher/results/results_ovito.dump", "lammps/dump",
                columns=["id", "Position.X", "Position.Y", "Position.Z", "Radius", "theta"], multiple_frames=True,
                start_frame=0, end_frame=len(data) - 1)


def get_particle_data(static_file, dynamic_file):
    st_df = pd.read_csv(static_file, sep='\\s', skiprows=2, names=["radius", "property"])
    st_df['radius'].replace([0], 1.0, inPlace=True)

    dynamic_info = []
    with open(dynamic_file, "r") as dynamic:
        next(dynamic)
        for line in dynamic:
            ll = line.split('\\s')
            if len(ll) > 1:
                line_info = []
                for index in ll:
                    line_info.append(float(index))
                dynamic_info.append(line_info)
            if ll == 1:
                return


if __name__ == '__main__':
    argv = sys.argv
    if len(argv) == 3:
        static_file = argv[1]
        dynamic_file = argv[2]
    else:
        raise ValueError(f'Please provide file with information.')

    export_to_ovito(static_file, dynamic_file)
