import * as React from "react";
import ApiClient from "../services/ApiClient";
import WeeklyStatsComponent from "./WeeklyStatsComponent"
import {WeeklyStats} from "./WeeklyStatsComponent";
import RadioButton from "../custom-classes/RadioButton";

class StateComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            user: '',
            state: '',
            dateTime: '',
            weeklyStats: WeeklyStats,
            userLogged: false
        };
        this.handleSubmitResult = this.handleSubmitResult.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }


    handleChange(event) {
        const name = event.target.name;
        this.setState({
            [name]: event.target.value
        });
    }

    handleSubmitResult(event) {
        event.preventDefault();
        ApiClient.sendState(this.state.user,
            this.state.state,
            this.state.dateTime)
            .then(res => {
                if (res.ok) {
                    this.updateMessage("New state was successfully logged");
                    this.setState({
                        userLogged: true
                    });
                    this.updateWeeklyStats(this.state.user);
                } else {
                    this.updateMessage("Error: server error or not available");
                }
            });
    }

    updateWeeklyStats(userAlias: string) {
        ApiClient.weeklyStats(userAlias).then(res => {
            if (res.ok) {
                res.json().then(stats => {
                    this.setState({
                        weeklyStats: stats
                    });
                });
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
                        Your name:
                        <input type="text" maxLength="12"
                               name="user"
                               value={this.state.user}
                               onChange={this.handleChange}/>
                        <br/>
                        <br/>
                        <div align="center">Select your current state:</div>
                        <br/>
                        <div className="radiobutton-div">
                            <RadioButton
                                id="bad-state"
                                text="Bad"
                                onChange={this.handleChange}
                                value="BAD"
                                styleColor="cornflowerblue"
                            />
                            <RadioButton
                                id="good-state"
                                text="Good"
                                onChange={this.handleChange}
                                value="GOOD"
                                styleColor="lightgreen"
                            />
                            <RadioButton
                                id="excellent-state"
                                text="Excellent"
                                onChange={this.handleChange}
                                value="EXCELLENT"
                                styleColor="seagreen"
                            />
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
                {this.state.userLogged &&
                <WeeklyStatsComponent weeklyStats={this.state.weeklyStats}/>}
            </div>
        );
    }
}

export default StateComponent;