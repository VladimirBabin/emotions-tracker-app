import * as React from "react";
import ApiClient from "../services/ApiClient";
import WeeklyStatsComponent from "./WeeklyStatsComponent"
import {WeeklyStats} from "./WeeklyStatsComponent";

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
                    this.state.userLogged = true;
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
                <div>
                    <h2>Welcome to emotions tracker</h2>
                </div>
                <form onSubmit={this.handleSubmitResult}>
                    <label className="states">
                        Your name:
                        <input type="text" maxLength="12"
                               name="user"
                               value={this.state.user}
                               onChange={this.handleChange}/>
                        <br/>
                        <br/>
                        Select your current state:
                        <br/>
                        <label>Bad
                            <input name="state"
                                   type="radio"
                                   value="BAD"
                                   onChange={this.handleChange} />
                        </label>
                        <label> Good
                            <input name="state"
                                   type="radio"
                                   value="GOOD"
                                   onChange={this.handleChange}/>
                        </label>
                        <label> Excellent
                            <input name="state"
                                   type="radio"
                                   value="EXCELLENT"
                                   onChange={this.handleChange}/>
                        </label>
                    </label>
                    <br/>
                    <input type="submit" value="Submit"/>
                </form>
                <h4>{this.state.message}</h4>
                {this.state.userLogged &&
                <WeeklyStatsComponent weeklyStats={this.state.weeklyStats}/>}
            </div>
        );
    }
}

export default StateComponent;