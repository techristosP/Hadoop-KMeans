import matplotlib.pyplot as plt

def read_data(file, output=False):
    with open(file, 'r') as f:
        lines = f.readlines()
    
    points = []
    for line in lines:
        values = line.split()
        if output:
            points.append((float(values[2]), float(values[3])))
        else:
            points.append((float(values[0]), float(values[1])))

    return points


def plot_data(points, centers):

    plt.scatter(*zip(*points))
    plt.plot(*zip(*centers), 'ro')
    for x, y in centers:
        plt.text(x, y, f'({x:.2f}, {y:.2f})', fontsize=9)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.title('Data points and real centers')
    # plt.legend()
    plt.show()


if __name__ == '__main__':
    points = read_data('datapoints.txt')
    centers = read_data('real-centers.txt', output=False)
    # centers = read_data('init-centers.txt', output=False)
    # centers = read_data('part-00000', output=True)
    plot_data(points, centers)

