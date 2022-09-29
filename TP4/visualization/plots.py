from pandas import DataFrame
import plotly.graph_objects as go


def plot(algorithm: DataFrame, analytic: DataFrame, title):
    data = []

    for row in algorithm.iterrows():
        data.append(go.Scatter(
            x = row["t"],
            y = row["x"],
            name = 'HOLIS',
            mode='lines'
        )),

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'{title}'),
            xaxis=dict(title="Time"),
            yaxis=dict(title="Position")
        )
    )
    fig.update_layout(width=1000, height=1000)
    fig.show()