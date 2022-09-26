from ovito_visualization import get_particle_data
import pandas as pd
import numpy as np

from plots import plot_small_particles_speed_distribution, plot_avg_collision_time, plot_big_particle_route, \
    plot_big_particle_coeff_distribution


def ej1():
    Ns = [100,110,125]
    L = 6
    times_ns = []
    speeds_ns:list[list] = []
    for i,n in enumerate(Ns):
        times_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/times_{n}.txt'
        static_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/static_{n}.txt'
        dynamic_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/dynamic_{n}.txt'
        times = pd.read_csv(times_file,skiprows=0,names=["time"])
        times_ns.insert(i,times.values)

    plot_avg_collision_time(times_ns, Ns)

def ej2():
    speeds_ns:list[list] = []
    Ns = [100,110,125]
    for i,n in enumerate(Ns):
        static_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/static_{n}.txt'
        dynamic_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/dynamic_{n}.txt'
        particles_data = get_particle_data(static_file, dynamic_file)
        speeds_ns.insert(i,[])
        for df in particles_data:
            if df[1]['mass'][1] == 0.9:
                speeds_ns[i].append(np.linalg.norm(df[1][["vx","vy"]],axis=1))
    plot_small_particles_speed_distribution(speeds_ns, Ns)

def ej3():
    L = 6
    speeds_values = [2,3,4]
    # speeds_values = [4]

    radio = 0
    kinetik_energy = 0
    big_particle_pos:list[list] = []
    for i,s in enumerate(speeds_values):
        static_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/static_v_{s}.txt'
        dynamic_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/dynamic_v_{s}.txt'
        particles_data = get_particle_data(static_file, dynamic_file)
        big_particle_pos.insert(i,[])

        for df in particles_data:
            if df[1]['mass'][0] == 2.0:
                radio = df[1]['r'][0]
                big_particle_pos[i].append([df[1]["x"][0],df[1]["y"][0]])
    plot_big_particle_route(big_particle_pos,radio, L)

def ej4_big_p():
    n=125
    data = []
    runs = []
    for i in range(25):
        static_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/static_{n}_{i}.txt'
        dynamic_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/dynamic_{n}_{i}.txt'
        particles_data = get_particle_data(static_file, dynamic_file)
        runs.append(i)
        data.insert(i,[])
        for df in particles_data:
            if df[1]['mass'][1] == 2.0:
                data[i].append([df[0],df[1]["x"][0],df[1]["y"][0]])
    print(data)
    plot_big_particle_coeff_distribution(data, runs)


if __name__ == '__main__':
    ej4_big_p()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
