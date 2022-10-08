import plotly.graph_objects as go
import numpy as np


def plot(all_states, title, legends):
    data = []
    for i in range(len(all_states)):
        x = []
        y = []
        for row in all_states[i].iterrows():
            x.append(row[1]['t'])
            y.append(row[1]['x'])
        data.append(go.Scatter(
            x=x,
            y=y,
            name=f'{legends[i]}',
            mode='lines',
            line={'dash': 'dash'}
        )),

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'{title}'),
            xaxis=dict(title="Time"),
            yaxis=dict(title="Position")
        ),
    )

    fig.show()


def plot_error(algorithm_name, all_errors, deltas):
    data = []
    for i, errors in enumerate(all_errors):
        data.append(go.Scatter(
            x=deltas,
            y=errors,
            name=f'{algorithm_name[i]}',
            mode='lines+markers',
        ))
    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'Error para {algorithm_name}'),
            xaxis=dict(title="Deltas"),
            yaxis=dict(title="Error cuadrático medio (m^2)")
        )
    )
    fig.update_layout(xaxis_type="log", yaxis_type="log")
    fig.show()


def plot_data(x, y, title, x_title, y_title):
    fig = go.Figure(
        data=go.Scatter(
            x=x,
            y=y,
        ),
        layout=go.Layout(
            title=dict(text=f'{title}'),
            xaxis=dict(title=f'{x_title}'),
            yaxis=dict(title=f'{y_title}')
        )
    )
    fig.show()
