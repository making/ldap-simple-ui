import React, {Component} from 'react';
import {Siteframe} from 'pivotal-ui/react/siteframe';
import {Icon} from 'pivotal-ui/react/iconography';

import 'pivotal-ui/css/typography';
import 'pivotal-ui/css/forms';
import userService from "./user/UserService";
import UserInfoFrame from "./user/UserInfoFrame";

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
        const primaryLinks = [
            {
                text: 'User Info', active: true, href: '#', onClick: this.activateLink.bind(this)
            }
        ];
        if (this.state.user.admin) {
            primaryLinks.push({
                text: 'User Management', active: false, href: '#', onClick: this.activateLink.bind(this)
            });
        }
        return <div style={{position: 'relative', height: '1000px'}}>
            <Siteframe {...{
                headerProps: {
                    logo: <div className="ptl pbl pll" style={{fontSize: '40px'}}><Icon src="pivotal_ui_inverted" style={{fill: 'currentColor'}}/></div>,
                    companyName: 'LDAP',
                    productName: 'Simple UI'
                },
                sidebarProps: {
                    primaryLinks: primaryLinks,
                    secondaryLinks: [{text: 'Logout', href: '/logout'}],
                    renderLink: ({text, href, onClick}) => <a href={href} onClick={onClick}>{text}</a>
                },
            }}>
                <UserInfoFrame user={this.state.user}
                               reload={this.loadUser.bind(this)}/>
            </Siteframe>
        </div>;
    }

    activateLink(event) {
        const li = event.target.parentElement.parentElement;
        li.parentNode.childNodes.forEach(n => {
            n.className = n === li ? 'pui-sidebar-li-active' : '';
        });
    }
}