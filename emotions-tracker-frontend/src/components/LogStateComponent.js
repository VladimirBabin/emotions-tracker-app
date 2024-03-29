import * as React from "react";
import EntryLoggingApiClient from "../services/EntryLoggingApiClient";
import RadioButton from "../helper_components/RadioButton";
import Checkbox from "../helper_components/Checkbox"
import './LogStateComponent.css'
import {getUserId} from "../services/AuthApiClient";
import FormDateTimeView from "../helper_components/FormDateTimeView";

import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';

dayjs.extend(utc);
dayjs.extend(timezone);

dayjs.tz.setDefault('Europe/Vilnius');


class LogStateComponent extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            state: '',
            emotions: [],
            message: '',
        };
        this.handleSubmitResult = this.handleSubmitResult.bind(this);
        this.handleSetChange = this.handleSetChange.bind(this);
        this.handleCheckbox = this.handleCheckbox.bind(this);
    }

    handleSetChange(event) {
        const name = event.target.name;
        this.setState({
            [name]: event.target.value
        });
    }

    handleCheckbox(event) {
        const value = event.target.value;
        if (event.target.checked === true) {
            this.state.emotions.push(value);
        } else {
            let filtered = this.state.emotions.filter(e => e !== value);
            this.setState({
                emotions: filtered
            })
        }
    }

    handleSubmitResult(event) {
        event.preventDefault();
        const dateTime = this.props.date === null ? null :
            dayjs.tz(this.props.date, true).toISOString();
        console.log(dateTime);
        EntryLoggingApiClient.sendState(getUserId(),
            this.state.state,
            this.state.emotions,
            dateTime)
            .then(res => {
                if (res.ok) {
                    this.updateMessage("New state was successfully logged");
                } else {
                    this.updateMessage("Error: server error or not available");
                }
            });
    }

    updateMessage(m: string) {
        this.setState({
            message: m
        });
    }

    render() {
        return (
            <div className="display-column">
                <form onSubmit={this.handleSubmitResult}>
                    <label className="states">
                        <br/> {!(this.props.date === null) &&
                        <div className="state-div">
                            <div className="state-title">
                                <FormDateTimeView value={this.props.date.toDate().toString()}/>
                            </div>
                        </div>}
                        <div className="state-div">
                            <div className="state-title">
                                <h6>{this.props.date === null ? "How are you?" : "How were you?"}</h6>
                            </div>
                            <div className="radiobutton-div">
                                <RadioButton
                                    text="Awful"
                                    onChange={this.handleSetChange}
                                    value="AWFUL"
                                />
                                <RadioButton
                                    text="Bad"
                                    onChange={this.handleSetChange}
                                    value="BAD"
                                />
                                <RadioButton
                                    text="Ok"
                                    onChange={this.handleSetChange}
                                    value="OK"
                                />
                                <RadioButton
                                    text="Good"
                                    onChange={this.handleSetChange}
                                    value="GOOD"
                                />
                                <RadioButton
                                    text="Excellent"
                                    onChange={this.handleSetChange}
                                    value="EXCELLENT"
                                />
                            </div>
                        </div>
                        <div className="state-div">
                            <div className="state-title">
                                <h6>{this.props.date === null ? "How do you feel?" : "How did you feel?"}</h6>
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Happy"
                                    onChange={this.handleCheckbox}
                                    value="HAPPY"
                                />
                                <Checkbox
                                    text="Indifferent"
                                    onChange={this.handleCheckbox}
                                    value="INDIFFERENT"
                                />
                                <Checkbox
                                    text="Sad"
                                    onChange={this.handleCheckbox}
                                    value="SAD"
                                />
                                <Checkbox
                                    text="Excited"
                                    onChange={this.handleCheckbox}
                                    value="EXCITED"
                                />
                                <Checkbox
                                    text="Peaceful"
                                    onChange={this.handleCheckbox}
                                    value="PEACEFUL"
                                />
                                <Checkbox
                                    text="Anxious"
                                    onChange={this.handleCheckbox}
                                    value="ANXIOUS"
                                />
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Satisfied"
                                    onChange={this.handleCheckbox}
                                    value="SATISFIED"
                                />
                                <Checkbox
                                    text="Content"
                                    onChange={this.handleCheckbox}
                                    value="CONTENT"
                                />
                                <Checkbox
                                    text="Drained"
                                    onChange={this.handleCheckbox}
                                    value="DRAINED"
                                />
                                <Checkbox
                                    text="Passionate"
                                    onChange={this.handleCheckbox}
                                    value="PASSIONATE"
                                />
                                <Checkbox
                                    text="Stressed"
                                    onChange={this.handleCheckbox}
                                    value="STRESSED"
                                />
                                <Checkbox
                                    text="Angry"
                                    onChange={this.handleCheckbox}
                                    value="ANGRY"
                                />
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Tired"
                                    onChange={this.handleCheckbox}
                                    value="TIRED"
                                />
                                <Checkbox
                                    text="Hopeful"
                                    onChange={this.handleCheckbox}
                                    value="HOPEFUL"
                                />
                                <Checkbox
                                    text="Irritated"
                                    onChange={this.handleCheckbox}
                                    value="IRRITATED"
                                />
                                <Checkbox
                                    text="Surprised"
                                    onChange={this.handleCheckbox}
                                    value="SURPRISED"
                                />
                                <Checkbox
                                    text="Scared"
                                    onChange={this.handleCheckbox}
                                    value="SCARED"
                                />
                                <Checkbox
                                    text="Jealous"
                                    onChange={this.handleCheckbox}
                                    value="JEALOUS"
                                />
                            </div>
                        </div>
                    </label>
                    <br/>
                    <div align="center">
                        <input type="submit" value="Submit"/>
                    </div>

                </form>
                <br/>
                <div>
                    {this.state.message}
                </div>
                <br/>
            </div>
        );
    }
}

export default LogStateComponent;