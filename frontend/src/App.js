import React, {Component} from 'react';
import {Siteframe} from 'pivotal-ui/react/siteframe';
import {Icon} from 'pivotal-ui/react/iconography';
import UserUpdatePanel from './user/UserUpdatePanel';
import PasswordUpdatePanel from "./password/PasswordUpdatePanel";

import 'pivotal-ui/css/typography';
import 'pivotal-ui/css/forms';
import userService from "./user/UserService";

export default class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: {}
        };
    }

    loadUser() {
        return userService.loadMe()
            .then(res => {
                const user = res.data;
                this.setState({
                    user: user
                });
            });
    }

    render() {
        return <div style={{position: 'relative', height: '1000px'}}>
            <Siteframe {...{
                headerProps: {
                    logo: <div className="ptl pbl pll" style={{fontSize: '40px'}}><Icon src="pivotal_ui_inverted" style={{fill: 'currentColor'}}/></div>,
                    companyName: 'LDAP',
                    productName: 'Simple UI'
                },
                sidebarProps: {
                    primaryLinks: [{text: 'User Info', active: true}],
                    secondaryLinks: [{text: 'Logout', href: '/logout'}],
                    renderLink: ({text, href}) => <a href={href}>{text}</a>
                },
            }}>
                <div className="bg-neutral-8 pal" style={{padding: '30px 30px 0px', height: '100%', overflow: 'auto'}}>
                    <UserUpdatePanel user={this.state.user} reload={this.loadUser.bind(this)}/>
                    <hr/>
                    <PasswordUpdatePanel user={this.state.user}/>
                </div>
            </Siteframe>
        </div>;
    }
}