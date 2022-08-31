import numpy as np
import pandas as pd
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import StaticSource, Pipeline

def export_to_ovito(static_file, dynamic_file, L, export_path):
    data_frame = get_particle_data(static_file, dynamic_file)

    # Create a new Pipeline with a StaticSource as data source:
    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    def simulation_cell(frame, data):
        # Insert a new SimulationCell object into a data collection:
        cell = SimulationCell(pbc=(False, False, False))
        cell[:, 0] = (L, 0, 0)
        cell[:, 1] = (0, L, 0)
        cell[:, 2] = (0, 0, 2)
        data.objects.append(cell)

        particles = get_particles(data_frame[frame])
        data.objects.append(particles)

    # Apply a modifier:
    pipeline.modifiers.append(simulation_cell)
    export_file(pipeline, export_path, 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Position.Z", "Radius", "angle", "Force.X", "Force.Y", "Force.Z"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def get_particle_data(static_file, dynamic_file):
    st_df = pd.read_csv(static_file, sep=" ", skiprows=2, names=["r", "property"])
    st_df['r'].replace([0], 1.0, inplace=True)

    dynamic_df = []
    with open(dynamic_file, "r") as dynamic:
        next(dynamic)
        dynamic_lines = []
        for line in dynamic:
            ll = line.split()
            line_info = []
            for index in ll:
                line_info.append(float(index))
            if len(line_info) > 1:
                dynamic_lines.append(line_info)
            elif len(line_info) == 1:
                df = pd.DataFrame(np.array(dynamic_lines), columns=["x", "y", "z", "theta", "speed"])
                dynamic_df.append(pd.concat([df, st_df], axis=1))
                dynamic_lines = []
        df = pd.DataFrame(np.array(dynamic_lines), columns=["x", "y", "z", "theta", "speed"])
        dynamic_df.append(pd.concat([df, st_df], axis=1))

    return dynamic_df


def get_particles(data_frame):
    particles = Particles()
    particles.create_property("Position", data=np.array((data_frame.x, data_frame.y, data_frame.z)).T)
    particles.create_property("Radius", data=data_frame.r)
    particles.create_property("angle", data=data_frame.theta)
    particles.create_property("Force", data=np.array((np.cos(data_frame.theta) * data_frame.speed, np.sin(data_frame.theta) * data_frame.speed, np.zeros(len(data_frame.x)))).T)
    return particles

