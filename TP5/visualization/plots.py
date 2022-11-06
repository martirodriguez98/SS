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

def plot_many_data(x,y,legends,title,x_title,y_title,legend):
    data = []
    for i in range(len(x)):
        data.append(go.Scatter(
            x=x[i],
            y=y[i],
            name=f'{legends[i]}',
            mode='lines+markers',
        )),

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'{title}'),
            xaxis=dict(title=f'{x_title}'),
            yaxis=dict(title=f'{y_title}'),
            legend=dict(title=f'{legend}'),
            font=dict(size=22)
        ),
    )

    fig.show()

def plot_error(w_s, flows, std_dev, title, x_title, y_title):
    fig = go.Figure(
        data=go.Scatter(
            x=w_s,
            y=flows,
            error_y=dict(array=std_dev)
        ),
        layout=go.Layout(
            title=dict(text=f'{title}'),
            xaxis=dict(title=f'{x_title}'),
            yaxis=dict(title=f'{y_title}'),
            font=dict(size=22)
        )
    )
    fig.show()

def plot_beverloo(d_s, mean, beverloo, title, x_title, y_title):
    data = []
    data.append(go.Scatter(
        x = d_s,
        y = mean,
        mode='markers',
        name='Algoritmo'
    ))
    data.append(go.Scatter(
        x = d_s,
        y = beverloo,
        mode='lines',
        name='Beverloo'

    ))
    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=title),
            xaxis=dict(title=x_title),
            yaxis=dict(title=y_title),
        )
    )
    fig.show()
