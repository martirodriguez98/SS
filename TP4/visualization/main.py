import pandas as pd

from plots import plot


def exercise_one():

    analytic = "/Users/martirodriguez/Documents/ITBA/SS/SS/TP4/algorithm/src/main/resources/analytic.txt"
    verlet = "/Users/martirodriguez/Documents/ITBA/SS/SS/TP4/algorithm/src/main/resources/verlet.txt"
    states_verlet = pd.read_csv(verlet, skiprows=0, names=["t","x","vx"])
    states_analytic = pd.read_csv(analytic, skiprows=0, names=["t","x","vx"])
    plot(states_verlet, states_analytic, "Verlet vs Analytic")

if __name__ == '__main__':
    exercise_one()

