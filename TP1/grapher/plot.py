from typing import List

import numpy as np
import matplotlib.pyplot as plt


def plot(particles, id):

    nb = particles[id - 1].neighbours
    fig = plt.figure()
    ax = fig.add_subplot()


    for p in particles:
        if p.id == id:
            ax.scatter(float(p.position.x) , float(p.position.y) , color='green')
            ax.annotate( p.id, (float(p.position.x) , float(p.position.y)))
        elif p in nb:
            ax.scatter(float(p.position.x) , float(p.position.y), color='red')
        else:
            ax.scatter(float(p.position.x), float(p.position.y), color='black')

    plt.show()
    # plt.savefig(f'plots/{id}.png')
