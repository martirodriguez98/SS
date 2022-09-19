from pandas import DataFrame
import plotly.graph_objects as go
import numpy as np

def plot_avg_collision_time(times_ns, n_values):
    data = []

    for i,times in enumerate(times_ns):
        avg_collision_freq = len(times)/times[-1]

        std_dev = np.std(times)
        counts, bin_edges = np.histogram(times, density=True)
        bin_centres = (bin_edges[:-1] + bin_edges[1:])/2

        data.append(go.Scatter(
            x=bin_centres,
            y=counts,
            mode='lines',
            name=f'N={n_values[i]}'
        ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'Colisiones', x=0.5),
            xaxis=dict(title='Tiempo de colisión (s)'),
            yaxis=dict(title='PDF'),
        )
    )
    fig.update_layout(width=1000,height=1000)
    fig.show()


def plot_small_particles_speed_distribution(speeds_ns, n_values):
    for i,speeds in enumerate(speeds_ns):
        data = []
        initial_speed = speeds[0]
        counts, bin_edges = np.histogram(initial_speed, density=True)
        bin_centres = (bin_edges[:-1] + bin_edges[1:])/2

        data.append(go.Scatter(
            x=bin_centres,
            mode="lines+markers",
            y=counts,
            name=f'Valores iniciales N={n_values[i]}'
        ))

        last_third = speeds[-int(len(speeds)/3):]
        counts, bin_edges = np.histogram(last_third, density=True)
        bin_centres = (bin_edges[:-1] + bin_edges[1:]) / 2.

        data.append(go.Scatter(
            x=bin_centres,
            mode="lines+markers",
            y=counts,
            name='Último tercio'
        ))

        fig = go.Figure(
            data=data,
            layout=go.Layout(
                title=dict(text=f'Speed N={n_values[i]}'),
                xaxis=dict(title=f'Rapidez (m/s)'),
                yaxis=dict(title="PDF")
            )
        )
        fig.update_layout(width=1000,height=1000)
        fig.show()

def plot_big_particle_route(big_particle_pos, kinetik_energy, radio):
    speeds = ["v=[0,2] m/s","v=[2,3] m/s", "v=[3,4] m/s"]
    fig = go.Figure(
        layout=go.Layout(
            xaxis=dict(title="Big particle route"),
            yaxis=dict(title="???")
        )
    )
    for i, pos in enumerate(big_particle_pos):
        size = len(big_particle_pos[i])
        fig.add_trace(go.Scatter(
            x=big_particle_pos[i][0:size,0],
            y=big_particle_pos[i][0:size,1],
            name=speeds[i],
            mode="lines",
        ))

        x = big_particle_pos[i][size-1,0]
        y = big_particle_pos[i][size-1,1]
        x0 = x - radio
        x1 = x + radio
        y0 = y - radio
        y1 = y + radio
        fig.add_shape(
            type="circle",
            xref="x", yref="y",
            x0=x0, y0=y0, x1=x1, y1=y1,
        )
    fig.update_layout(width=1000, height=1000)
    fig.show()



def plot_big_particle_coeff_distribution():
    pass

def plot_small_particles_coeff_distribution():
    pass