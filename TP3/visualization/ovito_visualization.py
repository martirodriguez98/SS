import pandas as pd
import numpy as np
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import Pipeline, StaticSource


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
    st_df = pd.read_csv(static_file, sep=" ", skiprows=2, names=["r", "mass"])

    dynamic_df = []
    count = 0
    prev_time =0
    with open(dynamic_file, "r") as dynamic:
        # next(dynamic)
        dynamic_lines = []
        for line in dynamic:
            ll = line.split()
            line_info = []
            for index in ll:
                line_info.append(float(index))
            if len(line_info) > 1:
                dynamic_lines.append(line_info)
            elif len(line_info) == 1:
                if count == 0:
                    prev_time = line_info[0]
                    count+=1
                else:
                    df = pd.DataFrame(np.array(dynamic_lines), columns=["x", "y", "z", "vx", "vy"])
                    dynamic_df.append(tuple([prev_time,pd.concat([df, st_df], axis=1)]))
                    dynamic_lines = []
                    prev_time = line_info[0]
        df = pd.DataFrame(np.array(dynamic_lines), columns=["x", "y", "z", "vx", "vy"])
        dynamic_df.append(tuple([line_info[0],pd.concat([df, st_df], axis=1)]))


    return dynamic_df


def get_particles(data_frame):
    particles = Particles()
    particles.create_property("Position", data=np.array((data_frame.x, data_frame.y, data_frame.z)).T)
    particles.create_property("Radius", data=data_frame.r)
    particles.create_property("angle", data=np.arctan(data_frame.vy,data_frame.vx))
    particles.create_property("Force", data=np.array((data_frame.vx, data_frame.vy, np.zeros(len(data_frame.x)))).T)

    return particles