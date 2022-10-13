import plotly.graph_objects as go

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
            xaxis=dict(title="Tiempo (s)"),
            yaxis=dict(title="Posición (m)"),
            font=dict(size=22)
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
            xaxis=dict(title="Deltas",exponentformat="power",type='log'),
            yaxis=dict(title="Error cuadrático medio (m^2)",exponentformat="power",type='log'),
            font=dict(size=22)
        )
    )
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
            yaxis=dict(title=f'{y_title}'),
            font=dict(size=22)
        )
    )
    fig.show()

def plot_different_v(velocities, distances, times):
    data = go.Scatter(
        x = velocities,
        y = times,
        mode='markers+text',
        textposition='top center',
        text = distances
    )

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'Tiempo de la misión hasta llegar'),
            xaxis=dict(title="Velocidad (km/s)"),
            yaxis=dict(title="Tiempo (días)"),
            font=dict(size=22)
        )
    )
    fig.show()