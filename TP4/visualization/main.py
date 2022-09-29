import pandas as pd

from plots import plot


def exercise_one():

    analytic = "analytic.txt"
    verlet = "verlet.txt"
    beeman = "beeman.txt"
    gear = "gear.txt"
    states_verlet = pd.read_csv(verlet, skiprows=0, names=['t','x','vx'])
    states_beeman = pd.read_csv(beeman, skiprows=0, names=["t","x","vx"])
    states_analytic = pd.read_csv(analytic, skiprows=0, names=["t","x","vx"])
    states_gear = pd.read_csv(gear, skiprows=0, names=["t","x","vx"])
    plot([states_analytic, states_verlet, states_beeman, states_gear], "Difference between algorithms",["Analytic", "Verlet", "Beeman", "Gear"])
    

if __name__ == '__main__':
    exercise_one()

