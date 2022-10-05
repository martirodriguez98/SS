import pandas as pd
import numpy as np
from ovito.data import DataCollection, SimulationCell, Particles
from collections import namedtuple

from ovito.io import export_file
from ovito.pipeline import Pipeline, StaticSource

EventData = namedtuple('EventData', ['time','data'])

def export_to_ovito():
    static_file = "static_file.txt"
    dynamic_file = "results.txt"
    export_path = "ovito_results.dump"
    data_frame = get_particle_data(static_file, dynamic_file, step=int(12*60*60/300))

    # Create a new Pipeline with a StaticSource as data source:
    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    def simulation_cell(frame, data):
        # Insert a new SimulationCell object into a data collection:
        cell = SimulationCell(pbc=(False, False, False), is2D=True)
        cell[:, 0] = (4, 0, 0)
        cell[:, 1] = (0, 2, 0)
        cell[:, 2] = (0, 0, 2)
        data.objects.append(cell)

        particles = get_particles(data_frame[frame].data)
        data.objects.append(particles)

    # Apply a modifier:
    pipeline.modifiers.append(simulation_cell)
    export_file(pipeline, export_path, 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Position.Z", "Radius", "Force.X", "Force.Y", "Force.Z"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def get_particle_data(static_file, dynamic_file,step):
    st_df = pd.read_csv(static_file, sep=",", skiprows=0, names=["name","radius","mass","x","y","vx","vy"], usecols=["radius","mass"])
    st_df.radius[0] = 1000
    st_df.radius[1] = 400
    st_df.radius[2] = 3000
    st_df.radius[3] = 800

    dfs = []
    with open(dynamic_file, "r") as results:
        current_frame_time = float(next(results))
        current_frame = []
        for line in results:
            float_vals = list(map(lambda v: float(v), line.split(',')))
            if len(float_vals) > 1:
                current_frame.append(float_vals)
            elif len(float_vals) == 1:
                df = pd.DataFrame(np.array(current_frame), columns=["id","x","y","vx","vy"])
                dfs.append(EventData(current_frame_time, pd.concat([df, st_df],axis=1)))
                current_frame = []
                current_frame_time = float_vals[0]
        df = pd.DataFrame(np.array(current_frame), columns=["id","x","y","vx","vy"])
        dfs.append(EventData(current_frame_time, pd.concat([df,st_df],axis=1)))
    return dfs[0::step]

def get_particles(data_frame):
    particles = Particles()
    particles.create_property('Particle Identifier', data=np.concatenate((data_frame.id, np.arange(len(data_frame.x),len(data_frame.x)))))
    particles.create_property("Position", data=np.array((data_frame.x, data_frame.y,np.zeros(len(data_frame.x)))).T)
    particles.create_property("Radius", data=data_frame.radius)
    particles.create_property("Force", data=np.array((data_frame.vx,data_frame.vy, np.zeros(len(data_frame.x)))).T)

    return particles