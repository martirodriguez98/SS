import pandas as pd
import numpy as np
from plots import plot, plot_error


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

def exercise_one_error():
    deltas = [0.01, 0.001,0.0001,0.00001]
    algorithms = ["verlet", "beeman", "gear"]
    all_errors = []
    for a in algorithms:
        states = []
        for dt in deltas:
            dt = np.format_float_positional(dt,trim='-')
            states.append(pd.read_csv(f'{a}_{dt}.txt', skiprows=0, names=['t','x','vx']))
        rs = []
        time = []
        xs = []
        error = []
        for i,state in enumerate(states):
            rs.append([])
            time.append([])
            xs.append([])
            error.append(0)
            for row in state.iterrows():
                time.append(row[1]["t"])
                xs[i].append(row[1]["x"])
                rs[i].append(analytic(row[1]["t"]))
            for x,y in zip(rs[i],xs[i]):
                error[i] = error[i] + (y-x) ** 2
        all_errors.append(error)

    plot_error(algorithms,all_errors,deltas)


def analytic(t):
    gamma = 100.0
    finalTime = 5.0
    A = 1.0
    k = 10000.0
    m = 70
    v = (-A * gamma)/(2*m)
    return A * (np.exp(-(gamma / (2 * m)) * t)) * (np.cos(np.power((k / m) - (gamma * gamma / (4 * (m * m))), 0.5) * t))

if __name__ == '__main__':
    exercise_one_error()

