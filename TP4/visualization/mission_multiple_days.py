import pandas as pd


from plots import plot_data


def parse_mission_results():

    days = pd.read_csv("datesMissionBack.txt", skiprows=0,names=["date","distance"])

    plot_data(days.loc[:,"date"].values,days.loc[:,"distance"].values, "Minimun value for launch day", "Día de despegue", "Distancia (km)")


if __name__ == '__main__':
    parse_mission_results()