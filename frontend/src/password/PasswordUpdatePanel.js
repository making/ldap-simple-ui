import React, {Component} from 'react';
import {PrimaryButton} from "pivotal-ui/react/buttons";
import {Form} from "pivotal-ui/react/forms";
import {FlexCol, Grid} from "pivotal-ui/react/flex-grids";
import {Input} from "pivotal-ui/react/inputs";
import {Panel} from "pivotal-ui/react/panels";
import {ErrorAlert, SuccessAlert} from "pivotal-ui/react/alerts";
import passwordService from "./PasswordService";
import Wrapper from "../utils/Wrapper";

class PasswordUpdatePanel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            success: false,
            error: null,
        };
    }

    render() {
        return (<Panel {...{title: 'Update Password', className: 'mtxl'}}>
            <SuccessAlert show={this.state.success}
                          dismissable={true}
                          onDismiss={Wrapper.dismiss(this)}>Updated successfully!</SuccessAlert>
            <ErrorAlert show={!!this.state.error}
                        dismissable={true}
                        onDismiss={Wrapper.dismiss(this)}>{this.state.error}</ErrorAlert>
            <Form {...{
                resetOnSubmit: true,
                onSubmit: ({initial, current}) => {
                    Wrapper.withHandler(
                        () => passwordService.updateMe(current.currentPassword, current.newPassword))(this);
                },
                fields: {
                    currentPassword: {
                        label: 'Current Password',
                        children: <Input type="password"/>
                    },
                    newPassword: {
                        label: 'New Password',
                        children: <Input type="password"/>
                    },
                    confirmPassword: {
                        label: 'Confirm Password',
                        help: 'Enter a matching password (button will remain disabled unless they match)',
                        retainLabelHeight: true,
                        children: <Input type="password" placeholder="Re-enter new password"/>
                    }
                }
            }}>
                {({state, fields, canSubmit}) => {
                    const passwIsValid = state.current.newPassword !== "" && state.current.newPassword === state.current.confirmPassword;
                    return (
                        <div>
                            <Grid>
                                <FlexCol>{fields.currentPassword}</FlexCol>
                                <FlexCol>{fields.newPassword}</FlexCol>
                                <FlexCol>{fields.confirmPassword}</FlexCol>
                            </Grid>
                            <Grid>
                                <FlexCol className="mtxxxl" fixed>
                                    <PrimaryButton type="submit" disabled={
                                        !passwIsValid || !canSubmit()
                                    }>Submit</PrimaryButton>
                                </FlexCol>
                            </Grid>
                        </div>
                    );
                }}
            </Form>
        </Panel>);
    }
}

export default PasswordUpdatePanel;