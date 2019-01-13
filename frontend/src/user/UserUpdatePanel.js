import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {PrimaryButton} from "pivotal-ui/react/buttons";
import {Form} from "pivotal-ui/react/forms";
import {FlexCol, Grid} from "pivotal-ui/react/flex-grids";
import {Input} from "pivotal-ui/react/inputs";
import {Panel} from "pivotal-ui/react/panels";
import {ErrorAlert, SuccessAlert} from "pivotal-ui/react/alerts";
import userService from "./UserService";
import Wrapper from "../utils/Wrapper";

class UserUpdatePanel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            success: false,
            error: null,
        };
    }

    render() {
        return (<Panel {...{title: 'Update UserInfo'}}>
            <SuccessAlert show={this.state.success}
                          dismissable={true}
                          onDismiss={Wrapper.dismiss(this)}>Updated successfully!</SuccessAlert>
            <ErrorAlert show={!!this.state.error}
                        dismissable={true}
                        onDismiss={Wrapper.dismiss(this)}>{this.state.error}</ErrorAlert>
            <Form {...{
                onSubmit: ({initial, current}) => {
                    Wrapper.withHandler(
                        () => userService.updateMe(current)
                            .then(this.props.reload.bind(this)))(this);
                },
                fields: {
                    dn: {
                        label: 'DN',
                        initialValue: this.props.user.dn,
                        children: <Input disabled={true}/>
                    },
                    id: {
                        label: 'ID',
                        initialValue: this.props.user.id,
                        children: <Input disabled={true}/>
                    },
                    email: {
                        label: 'Email',
                        initialValue: this.props.user.email,
                    },
                    firstName: {
                        label: 'First Name',
                        initialValue: this.props.user.firstName,
                    },
                    lastName: {
                        label: 'Last Name',
                        initialValue: this.props.user.lastName,
                    },
                }
            }}>
                {({fields, canSubmit}) => {
                    return (
                        <div>
                            <Grid>
                                <FlexCol>{fields.dn}</FlexCol>
                            </Grid>
                            <Grid>
                                <FlexCol>{fields.id}</FlexCol>
                                <FlexCol>{fields.email}</FlexCol>
                            </Grid>
                            <Grid>
                                <FlexCol>{fields.firstName}</FlexCol>
                                <FlexCol>{fields.lastName}</FlexCol>
                            </Grid>
                            <Grid>
                                <FlexCol className="mtxxxl" fixed>
                                    <PrimaryButton type="submit" disabled={!canSubmit()}>Submit</PrimaryButton>
                                </FlexCol>
                            </Grid>
                        </div>
                    );
                }}
            </Form>
        </Panel>);
    }

    componentDidMount() {
        Wrapper.withErrorHandler(this.props.reload.bind(this))(this);
    }

    static propTypes = {
        user: PropTypes.object,
        reload: PropTypes.func
    };
}

export default UserUpdatePanel;