import React, { Component } from 'react';
import { Row, Col, Menu, Spin, notification, Button, Layout,
    Input, Modal, Form, Checkbox, Upload, Icon, List, Avatar } from 'antd';
import { Link } from 'react-router-dom';

const { Header, Footer, Content } = Layout;
const Search = Input.Search;
const FormItem = Form.Item;

const data = [
    {
      title: 'Ant Design Title 1',
    },
    {
      title: 'Ant Design Title 2',
    },
    {
      title: 'Ant Design Title 3',
    },
    {
      title: 'Ant Design Title 4',
    },
  ];

class InicioScreen extends Component {

    state = {
        loading: false,
        openModal: false,
        visible: false,
        checkNick: false,
    };

    normFile = (e) => {
        console.log('Upload event:', e);
        if (Array.isArray(e)) {
          return e;
        }
        return e && e.fileList;
      }

    check = () => {
        this.props.form.validateFields(
          (err) => {
            if (!err) {
              console.info('success');
            }
          },
        );
      }
    
      handleChange = (e) => {
        this.setState({
          checkNick: e.target.checked,
        }, () => {
          this.props.form.validateFields(['nickname'], { force: true });
        });
      }

    showModal = () => {
        this.setState({
          visible: true,
        });
    }
    
    handleOk = () => {
    this.setState({ openModal: true });
    setTimeout(() => {
        this.setState({ openModal: false, visible: false });
    }, 3000);
    }

    handleCancel = () => {
    this.setState({ visible: false });
    }

    openNotificationWithIcon = (type, message, description) => {
        notification[type]({
            message: message,
            description: description,
        });
    };

    renderList(){
        return (
            <List
                itemLayout="horizontal"
                dataSource={data}
                renderItem={item => (
                <List.Item>
                    <List.Item.Meta
                    avatar={<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />}
                    title={<a href="https://ant.design">{item.title}</a>}
                    description="Ant Design, a design language for background applications, is refined by Ant UED Team"
                    />
                </List.Item>
                )}
            />
        );
    }

    renderForm(){
        const { getFieldDecorator } = this.props.form;

        return (
            <div>
              <FormItem
                label="Material"
                extra="Selecione um arquivo"
                >
                {getFieldDecorator('upload', {
                    valuePropName: 'fileList',
                    getValueFromEvent: this.normFile,
                })(
                    <Upload name="logo" action="/upload.do" listType="picture">
                    <Button>
                        <Icon type="upload" /> Procurar...
                    </Button>
                    </Upload>
                )}
            </FormItem>
              <FormItem label="Titulo do material">
                {getFieldDecorator('nickname', {
                  rules: [{
                    required: this.state.checkNick,
                    message: 'Por favor, informe um titulo para o material',
                  }],
                })(
                  <Input placeholder="Titulo do material" />
                )}
              </FormItem>
              <FormItem label="Descrição">
                {getFieldDecorator('nickname', {
                  rules: [{
                    required: this.state.checkNick,
                    message: 'Por favor, informe uma descrição para o material',
                  }],
                })(
                  <Input placeholder="Descrição do material" />
                )}
              </FormItem>
            </div>
          );
    }

    renderModal(){
        const { visible, openModal } = this.state;

        return (
            <Modal
                visible={visible}
                title="Fazer Upload de Material"
                onOk={this.handleOk}
                onCancel={this.handleCancel}
                footer={[
                    <Button
                    key="back"
                    style={{ 'background': '#a7131b', 'color': 'white', 'borderColor': '#a7131b' }}
                    onClick={this.handleCancel}
                    >
                        Cancelar
                    </Button>,
                    <Button
                    key="submit"
                    style={{ 'background': '#2e9031', 'color': 'white', 'borderColor': '#2e9031' }}
                    loading={openModal}
                    onClick={this.handleOk}
                    >
                        Salvar
                    </Button>,
                ]}
                >
                {this.renderForm()}
            </Modal>
        );
    }

    renderContent() {
        return (
            <Layout>
                <Header style={{ backgroundColor: '#ffffff' }}>
                    <Row type="flex" justify="end">
                        <Col span={1}>
                            <Button
                                type="primary"
                                icon="upload"
                                style={{ width: '100%', height: '45px', fontSize: '20px' }}
                                onClick={this.showModal}
                            />
                        </Col>
                    </Row>
                </Header>
                <Content style={{ backgroundColor: '#ffffff' }}>
                    {this.renderModal()}
                    <Row>
                        <Col span={16} offset={4}>
                            <Row type="flex" justify="center" align="middle">
                                <Col span={3}>
                                    <img type="flex" alt="ISC" src="../meet.png" height="60" width="60"/>
                                </Col>
                            </Row>
                            
                            <Row type="flex" justify="center" align="middle">
                                <Col span={6}>
                                    <h1 style={{ 'fontFamily': 'Montserrat' }}>
                                        IsCool Shared
                                    </h1>
                                </Col>
                            </Row>
                            <Row type="flex" justify="center" align="middle">
                                <Col span={12}>
                                    <Search
                                        placeholder="Procurar Material"
                                        onSearch={value => console.log(value)}
                                        style={{ height: '45px' }}
                                        onClick = {() => {}}
                                    />

                                </Col>
                            </Row>
                        </Col>
                    </Row>
                    <Row
                    type="flex"
                    justify="center"
                    align="middle"
                    style={{ 'marginTop': '2em' }}
                    >
                        <Col span={12}>
                            {this.renderList()}
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
                    {this.renderContent()}
                </Spin>
            )
        }
        return this.renderContent();
    }
}

export default Form.create()(InicioScreen);
