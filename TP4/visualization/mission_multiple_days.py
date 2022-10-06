from export_ovito import export_to_ovito
import pandas as pd
import numpy as np
import datetime

from plots import plot_data
from utils import parse_data, get_spaceship_distance


def parse_mission_results():
    # days = ['23-09-2022', '24-09-2022', '25-09-2022','06-10-2022', '20-10-2022','23-10-2022', '01-12-2023','25-09-2023']
    days = ['25-09-2022']

    [initial_date, min_distance] = [[],[]]
    for day in days:
        static_file = f'static-file-{day}.txt'
        dynamic_file = f'results-{day}.txt'
        export_file = f'ovito-results-{day}.dump'
        export_to_ovito(static_file, dynamic_file, export_file)

        data = parse_data(static_file,dynamic_file)
        print(f'parse for day {day} complete')
        date_tokens = day.split('-')
        date = datetime.date(int(date_tokens[2]),int(date_tokens[1]),int(date_tokens[0]))
        [dates, distances] = get_spaceship_distance(data, date)
        plot_data(dates, distances, f'Spaceship distance to Venus launch day {day}', "Date", "Distance")
        initial_date.append(day)
        min_distance.append(min(distances))
        print(f'day: {day} complete!')

    plot_data(initial_date,min_distance, "Minimun value for launch day", "Launch day", "Distance")




if __name__ == '__main__':
    parse_mission_results()