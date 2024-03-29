import './setup';
import cluster from 'cluster';
import { createServer } from 'http';
import { cpus } from 'os';

import app from './app';
import { env, port } from './config/server';

const fork = () => {
    // Fork workers
    cpus().forEach(() => {
        cluster.fork();
    });

    return cluster.on('exit', worker => {
        // eslint-disable-next-line
        console.info(`Worker ${worker.process.pid} died`);
        return cluster.fork();
    });
};

const listen = () => (
    // Workers can share any TCP connection
    // In this case it is an HTTP server
    createServer(app)
        .listen(port, () => {
            // eslint-disable-next-line
            console.info(`Worker ${process.pid} started on port ${port}`);
        })
);

const start = () => {
    if (true || env === 'development') { // eslint-disable-line
        return listen();
    }

    return fork();
};

if (cluster.isMaster) {
    /* eslint-disable no-console */
    console.info(`Using "${env}" environment`);
    console.info(`Master ${process.pid} is running`);
    /* eslint-enable no-console */

    start();

} else {
    listen();
}
