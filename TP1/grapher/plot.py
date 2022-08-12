from typing import List

import numpy as np
import matplotlib.pyplot as plt


def plot(particles, id):

    plt.style.use('dark_background')
    nb = particles[id - 1].neighbours
    fig = plt.figure()
    ax = fig.add_subplot()

    for p in particles:
        if p.id == id:
            ax.scatter(float(p.position.x) , float(p.position.y) , color='#ff1493')
            ax.annotate( p.id, (float(p.position.x) , float(p.position.y)))
        elif p in nb:
            ax.scatter(float(p.position.x) , float(p.position.y), color='#91db57')
        else:
            ax.scatter(float(p.position.x), float(p.position.y), color='white')

    plt.show()
    # plt.savefig(f'plots/{id}.png')

def plot_time(time, mode):
    plt.style.use('dark_background')
    fig = plt.figure()
    ax = fig.add_subplot()
    new_time = []
    for t in time:
        new_time.append(float(t.replace('\n','')))

    n_list = list(reversed(list(range(1,len(time)+1))))

    new_list = zip(*sorted(zip(n_list, new_time)))
    plt.plot(*new_list)
    plt.xlabel("Amount of particles (N)")
    plt.ylabel("Time (miliseconds)")
    plt.title("Relation execution time / amount of particles - " + mode)
    plt.show()