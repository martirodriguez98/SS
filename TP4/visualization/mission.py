import datetime

from export_ovito import export_to_ovito
from plots import plot_data
from utils import parse_data, get_spaceship_distance, get_spaceship_velocities

def run_mission():
    day = '23-09-2022'
    static_file = f'static-file-{day}.txt'
    dynamic_file = f'results-{day}.txt'
    export_file = f'ovito-results-{day}.dump'

    date_tokens = day.split('-')
    initial_date = datetime.date(int(date_tokens[2]), int(date_tokens[1]), int(date_tokens[0]))

    data = parse_data(static_file, dynamic_file)
    print(f'parse for day {day} complete')

    # export_to_ovito(static_file, dynamic_file, export_file)
    # print(f'Ovito exportation for day {day} complete')
    #
    # eja(data,initial_date, day) #day is string and initial date is datetime

    ejb(data,initial_date,day)

    print(f'day: {day} complete!')

def eja(data, initial_date, day):
    [dates, distances] = get_spaceship_distance(data, initial_date)
    plot_data(dates, distances, f'Spaceship distance to Venus launch day {day}', "Date", "Distance")

def ejb(data, initial_date, day):
    [dates,velocities_data] = get_spaceship_velocities(data, initial_date)
    plot_data(dates, velocities_data, f'Spaceship speed evolution for launch day {day}', "Date", "Velocity")


if __name__ == '__main__':
    run_mission()


