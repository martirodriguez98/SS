import pandas as pd
import numpy as np
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import Pipeline, StaticSource


def export_to_ovito():
    static_file = "static_file.txt"
    dynamic_file = "results.txt"
    export_path = "ovito_results.dump"
    data_frame = get_particle_data(static_file, dynamic_file)

    # Create a new Pipeline with a StaticSource as data source:
    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    def simulation_cell(frame, data):
        # Insert a new SimulationCell object into a data collection:
        cell = SimulationCell(pbc=(False, False, False))
        cell[:, 0] = (4, 0, 0)
        cell[:, 1] = (0, 2, 0)
        cell[:, 2] = (0, 0, 2)
        data.objects.append(cell)

        particles = get_particles(data_frame[frame])
        data.objects.append(particles)

    # Apply a modifier:
    pipeline.modifiers.append(simulation_cell)
    export_file(pipeline, export_path, 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Position.Z", "Radius", "Velocity.X", "Velocity.Y", "Velocity.Z"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def get_particle_data(static_file, dynamic_file):
    st_df = pd.read_csv(static_file, sep=",", skiprows=0, names=["name","radius","mass","x","y","vx","vy"], usecols=["radius","mass"])
    st_df.mass[0] = 200
    st_df.mass[1] = 200
    st_df.mass[2] = 0
    st_df.mass[3] = 200

    dynamic_df = []
    count = 0
    prev_time =0
    with open(dynamic_file, "r") as dynamic:
        # next(dynamic)
        dynamic_lines = []
        for line in dynamic:
            ll = line.split(',')
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
    particles.create_property("Position", data=np.array((data_frame[1].x, data_frame[1].y, data_frame[1].z)).T)
    particles.create_property("Radius", data=data_frame[1].radius)
    particles.create_property("Velocity", data=np.array((data_frame[1].vx,data_frame[1].vy, np.zeros(len(data_frame[1].x)))).T)

    return particles