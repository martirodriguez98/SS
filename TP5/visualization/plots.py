import plotly.graph_objects as go

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