import React, { Component } from 'react';
import { Row, Col, Menu, Spin, notification, Button, Input } from 'antd';
import { Link } from 'react-router-dom';
import { Layout } from 'antd';

const { Header, Footer, Content } = Layout;
const Search = Input.Search;

export default class InicioScreen extends Component {

    state = {
        loading: false,
    };

    

    openNotificationWithIcon = (type, message, description) => {
        notification[type]({
            message: message,
            description: description,
        });
    };

    renderFormulario() {
        return (
            <Layout>
                <Header style={{ backgroundColor: '#ffffff' }}>
                    <Row type="flex" justify="end">
                        <Col span={1}>
                            <Button type="primary" icon="upload" style={{ width: '100%', height: '45px', fontSize: '20px' }} />
                        </Col>
                    </Row>
                </Header>
                <Content style={{ backgroundColor: '#ffffff' }}>
                    <Row>
                        <Col span={16} offset={4}>
                            <Row type="flex" justify="center" align="middle">
                                <Col span={6}>
                                    <h1>Is Cool Shared</h1>
                                </Col>
                            </Row>
                            <Row type="flex" justify="center"  align="middle">
                                <Col span={12}>
                                    <Search
                                        placeholder="input search text"
                                        onSearch={value => console.log(value)}
                                        style={{ height: '45px' }}
                                        onClick = {()=> ({

                                        })}
                                    />

                                </Col>
                            </Row>
                        </Col>
                    </Row>
                </Content>
            </Layout>
        );
    }

    render() {
        if (this.state.loading) {
            return (
                <Spin tip="Carregando...">
                    {this.renderFormulario()}
                </Spin>
            )
        }

        return this.renderFormulario();
    }
}
