import os
import random, math, matplotlib.pyplot as plt

def create_points(centers, num_points):

    n_clusters = []
    n_points = 0
    batch_size = num_points // len(centers)
    for c in range(len(centers)):
        n_clusters.append((n_points, n_points + batch_size))
        n_points += batch_size
    

    points = []
    for n_cl in n_clusters:
        for i in range(n_cl[0], n_cl[1]):
            center = centers[n_clusters.index(n_cl)]
            distance = random.expovariate(0.05)
            angle = random.uniform(0, 2 * math.pi)

            x = center[0] + distance * math.cos(angle)
            y = center[1] + distance * math.sin(angle)

            points.append((x, y))


    return points

def write_to_file(points, filename):
    path = os.getcwd() + '/input/' + filename
    #path = os.getcwd() + '/' + filename
    with open(path, 'w') as f:
        for point in points:
            f.write(f'{point[0]} {point[1]}\n')

if __name__ == '__main__':

    centers = [(-100, 200), (300, 300), (0, -100)]  # centers of clusters
    num_points = 10**6+3                            # number of points

    points = create_points(centers, num_points)     # create points
    write_to_file(points, 'datapoints.txt')         # write points to file

    print(f'Points created: {len(points)} \n')
    print(points[:10])                              # print first 10 points

    plt.scatter(*zip(*points))                      # plot points
    plt.plot(*zip(*centers), 'ro')                  # plot centers
    plt.show()                                      # show plot