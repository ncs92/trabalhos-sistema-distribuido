import React, { Component } from 'react';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';

import "antd/dist/antd.css";
import "./assets/css/App.css";
import "./assets/css/FormEnterSystem.css";
import "./assets/css/Main.css";
import "./assets/css/Search.css";

import InicioScreen from './pages/InicioScreen';


import "../node_modules/react-image-gallery/styles/scss/image-gallery.scss";
import "../node_modules/react-image-gallery/styles/css/image-gallery.css";
import "../node_modules/react-image-gallery/styles/css/image-gallery.css";
import "../node_modules/react-image-gallery/styles/scss/image-gallery-no-icon.scss";
import "../node_modules/react-image-gallery/styles/css/image-gallery-no-icon.css";

import axios from 'axios';
import {
    setTokenUsuario,
    setUsuario,
    getTokenUsuario,
    isCurador,
    isCuradorOuOperador,
} from './helpers/usuarios';

axios.defaults.baseURL = 'http://localhost:3003/api';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.interceptors.request.use(config => {
    config.headers['token'] = getTokenUsuario();
    return config;
});



export default class App extends Component {

    constructor() {
        super();

        const token = localStorage.getItem('token');
        setTokenUsuario(token);

        const usuario = localStorage.getItem('usuario');
        if (usuario) {
            setUsuario(JSON.parse(usuario));
        }
    }


    render() {
        return (
            <BrowserRouter>
                <Switch>
                    <Route path="/" component={InicioScreen} />
                </Switch>
            </BrowserRouter>
        );
    }
}
