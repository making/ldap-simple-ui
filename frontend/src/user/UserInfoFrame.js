import React, {Component} from 'react';
import PropTypes from 'prop-types';
import UserUpdatePanel from "./UserUpdatePanel";
import PasswordUpdatePanel from "../password/PasswordUpdatePanel";

class UserInfoFrame extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (<div className="bg-neutral-8 pal" style={{padding: '30px 30px 0px', height: '100%', overflow: 'auto'}}>
            <UserUpdatePanel user={this.props.user} reload={this.props.reload}/>
            <hr/>
            <PasswordUpdatePanel user={this.props.user}/>
        </div>);
    }

    static propTypes = {
        user: PropTypes.object,
        reload: PropTypes.func
    };
}

export default UserInfoFrame;