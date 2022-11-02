import math
import numpy as np
import pandas as pd
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import StaticSource, Pipeline


def _generate_opening(D,W,L, time):
    vibration = 0.15 * math.sin(W * time)
    left_wall = np.array([[0, y + vibration, 0] for y in np.arange(0, L, 0.5)])
    right_wall = np.array([[W, y + vibration, 0] for y in np.arange(0, L, 0.5)])
    bottom_wall_left = np.array([[x, vibration, 0] for x in np.arange(0, W / 2 - D / 2 + 0.5, 0.5)])
    bottom_wall_right = np.array([[x, vibration, 0] for x in np.arange(W / 2 + D / 2, W + 0.5, 0.5)])
    return np.concatenate((left_wall, right_wall, bottom_wall_left, bottom_wall_right))


def export_to_ovito(static_file, dynamic_file, L,W, D,export_path):
    data_frame = get_particle_data(static_file, dynamic_file)

    # Create a new Pipeline with a StaticSource as data source:
    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    def simulation_cell(frame, data):
        # Insert a new SimulationCell object into a data collection:
        cell = SimulationCell(pbc=(False, False, False))
        cell[:, 0] = (W, 0, 0)
        cell[:, 1] = (0, L, 0)
        cell[:, 2] = (0, 0, 2)
        data.objects.append(cell)

        particles = get_particles(data_frame[frame],D,W,L)
        data.objects.append(particles)

    # Apply a modifier:
    pipeline.modifiers.append(simulation_cell)
    export_file(pipeline, export_path, 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Position.Z", "Radius", "Force.X", "Force.Y", "Force.Z","Wall"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def get_particle_data(static_file, dynamic_file):
    st_df = pd.read_csv(static_file, sep=",", skiprows=1, names=["id","r", "mass"])

    dynamic_df = []
    with open(dynamic_file, "r") as dynamic:
        next(dynamic)
        dynamic_lines = []
        for line in dynamic:
            ll = line.split(",")
            line_info = []
            for index in ll:
                line_info.append(float(index))
            if len(line_info) > 1:
                dynamic_lines.append(line_info)
            elif len(line_info) == 1:
                df = pd.DataFrame(np.array(dynamic_lines), columns=["x", "y", "vx", "vy"])
                dynamic_df.append([line_info[0],pd.concat([df, st_df], axis=1)])
                dynamic_lines = []
        df = pd.DataFrame(np.array(dynamic_lines), columns=["x", "y", "vx","vy"])
        dynamic_df.append([line_info[0],pd.concat([df, st_df], axis=1)])

    return dynamic_df


def get_particles(data_frame,D,W,L):
    particles = Particles()
    opening_points = _generate_opening(D,W,L,data_frame[0])
    particles.create_property('Particle Identifier', data=np.concatenate(((data_frame[1].id.values, np.full(len(opening_points), max(data_frame[1].id.values) +1)))))
    particles.create_property("Position", data=np.concatenate((np.array((data_frame[1].x, data_frame[1].y, np.zeros(len(data_frame[1].x)))).T, opening_points)))
    particles.create_property("Radius", data=np.concatenate((data_frame[1].r, np.full(len(opening_points), 0.2))))
    particles.create_property("Force", data=np.concatenate((np.array((data_frame[1].vx, data_frame[1].vy, np.zeros(len(data_frame[1].x)))).T, np.zeros((len(opening_points),3)))))
    particles.create_property('Wall', data=np.concatenate((np.full(len(data_frame[1].id),0.2), np.full(len(opening_points),0))))
    return particles

