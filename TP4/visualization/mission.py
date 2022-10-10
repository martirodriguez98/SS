import datetime

from export_ovito import export_to_ovito
import numpy as np
from plots import plot_data
from utils import parse_data, get_spaceship_distance, get_spaceship_velocities


def run_mission():
    day = '17-08-2023'
    static_file = f'static-file-{day}.txt'
    dynamic_file = f'results-{day}.txt'
    export_file = f'ovito-results-{day}.dump'

    date_tokens = day.split('-')
    initial_date = datetime.date(int(date_tokens[2]), int(date_tokens[1]), int(date_tokens[0]))

    data = parse_data(static_file, dynamic_file)
    print(f'parse for day {day} complete')

    export_to_ovito(static_file, dynamic_file, export_file)
    print(f'Ovito exportation for day {day} complete')


    [dates, distances] = get_spaceship_distance(data, initial_date)
    # plot_data(dates, distances, f'Spaceship distance to Venus launch day {day}', "Date", "Distance")
    #
    min_distance_index = np.argmin(distances)
    #
    # [dates,velocities_data] = get_spaceship_velocities(data, initial_date, min_distance_index)
    # plot_data(dates, velocities_data, f'Spaceship speed evolution for launch day {day}', "Date", "Velocity")

    min_distance_data = data[min_distance_index]
    relative_distance = np.sqrt((min_distance_data[1].iloc[2]['vx'] - min_distance_data[1].iloc[3]['vx']) ** 2 + (min_distance_data[1].iloc[2]['vy'] - min_distance_data[1].iloc[3]['vy']) ** 2)
    arrival_day = initial_date + datetime.timedelta(seconds=min_distance_data[0])
    print(f'Spaceship\'s relative distance to venus on arrival day {arrival_day} is {relative_distance}')

    print(f'day: {day} complete!')


if __name__ == '__main__':
    run_mission()


