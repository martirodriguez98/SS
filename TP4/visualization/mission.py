import datetime

from export_ovito import export_to_ovito
import numpy as np
import pandas as pd

from plots import plot_data, plot_different_v
from utils import parse_data, get_spaceship_distance, get_spaceship_velocities


def run_mission():
    day = '17-08-2023'
    # static_file = f'static-file-{day}.txt'
    # dynamic_file = f'results-{day}.txt'
    # export_file = f'ovito-results-{day}.dump'
    static_file = f'missionBackStatic-{day}.txt'
    dynamic_file = f'missionBack-{day}.txt'
    export_file = f'ovitoMissionBack-{day}.dump'

    date_tokens = day.split('-')
    initial_date = datetime.date(int(date_tokens[2]), int(date_tokens[1]), int(date_tokens[0]))

    data = parse_data(static_file, dynamic_file)
    print(f'parse for day {day} complete')

    export_to_ovito(static_file, dynamic_file, export_file)
    print(f'Ovito exportation for day {day} complete')


    [dates, distances] = get_spaceship_distance(data, initial_date)
    plot_data(dates, distances, f'Distancia de la nave a venus para el despegue {day}', "Días", "Distancia (km)")

    min_distance_index = np.argmin(distances) + 1

    [dates,velocities_data] = get_spaceship_velocities(data, initial_date, min_distance_index)
    plot_data(dates, velocities_data, f'Evolución de la velocidad despegue {day}', "Días", "Velocidad (km/s)")

    min_distance_data = data[min_distance_index]
    relative_distance = np.sqrt((min_distance_data[1].iloc[2]['vx'] - min_distance_data[1].iloc[3]['vx']) ** 2 + (min_distance_data[1].iloc[2]['vy'] - min_distance_data[1].iloc[3]['vy']) ** 2)
    arrival_day = initial_date + datetime.timedelta(seconds=min_distance_data[0])
    print(f'Spaceship\'s relative distance to venus on arrival day {arrival_day} is {relative_distance}')

    print(f'day: {day} complete!')


def different_velocities():
    file = "different_v.txt"
    info = pd.read_csv(file,skiprows=0,sep=",",names=["velocity", "distance", "time"])
    distances = info.loc[:,"distance"].values
    text_distances = []
    for i,d in enumerate(distances):
        text_distances.append(f'{round(d,4)} km')
    plot_different_v(info.loc[:,"velocity"].values,text_distances,info.loc[:,"time"].values)



if __name__ == '__main__':
    run_mission()
    # different_velocities()

