import * as React from "react";
import ApiClient from "../services/ApiClient";
import WeeklyStatsComponent from "./WeeklyStatsComponent"
import {WeeklyStats} from "./WeeklyStatsComponent";
import RadioButton from "../custom-classes/RadioButton";
import Checkbox from "../custom-classes/Checkbox"
import './StateComponent.css'

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
                        <div className="state-div">
                            <div className="state-title">
                                <h6>How are you?</h6>
                            </div>
                            <div className="radiobutton-div">
                                <RadioButton
                                    text="Awful"
                                    onChange={this.handleChange}
                                    value="AWFUL"
                                    styleColor="indigo"
                                />
                                <RadioButton
                                    text="Bad"
                                    onChange={this.handleChange}
                                    value="BAD"
                                    styleColor="cornflowerblue"
                                />
                                <RadioButton
                                    text="Ok"
                                    onChange={this.handleChange}
                                    value="OK"
                                    styleColor="yellowgreen"
                                />
                                <RadioButton
                                    text="Good"
                                    onChange={this.handleChange}
                                    value="GOOD"
                                    styleColor="mediumseagreen"
                                />
                                <RadioButton
                                    text="Excellent"
                                    onChange={this.handleChange}
                                    value="EXCELLENT"
                                    styleColor="seagreen"
                                />
                            </div>
                        </div>
                        <div className="state-div">
                            <div className="state-title">
                                <h6>How do you feel?</h6>
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Happy"
                                    onChange={this.handleChange}
                                    value="HAPPY"
                                    styleColor="yellow"
                                />
                                <Checkbox
                                    text="Indifferent"
                                    onChange={this.handleChange}
                                    value="INDIFFERENT"
                                    styleColor="skyblue"
                                />
                                <Checkbox
                                    text="Sad"
                                    onChange={this.handleChange}
                                    value="SAD"
                                    styleColor="rebeccapurple"
                                />
                                <Checkbox
                                    text="Excited"
                                    onChange={this.handleChange}
                                    value="EXCITED"
                                    styleColor="deeppink"
                                />
                                <Checkbox
                                    text="Peaceful"
                                    onChange={this.handleChange}
                                    value="PEACEFUL"
                                    styleColor="darkolivegreen"
                                />
                                <Checkbox
                                    text="Anxious"
                                    onChange={this.handleChange}
                                    value="ANXIOUS"
                                    styleColor="cyan"
                                />
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Satisfied"
                                    onChange={this.handleChange}
                                    value="SATISFIED"
                                    styleColor="lightpink"
                                />
                                <Checkbox
                                    text="Content"
                                    onChange={this.handleChange}
                                    value="CONTENT"
                                    styleColor="indianred"
                                />
                                <Checkbox
                                    text="Drained"
                                    onChange={this.handleChange}
                                    value="DRAINED"
                                    styleColor="black"
                                />
                                <Checkbox
                                    text="Passionate"
                                    onChange={this.handleChange}
                                    value="PASSIONATE"
                                    styleColor="darkorange"
                                />
                                <Checkbox
                                    text="Stressed"
                                    onChange={this.handleChange}
                                    value="STRESSED"
                                    styleColor="powderblue"
                                />
                                <Checkbox
                                    text="Angry"
                                    onChange={this.handleChange}
                                    value="ANGRY"
                                    styleColor="crimson"
                                />
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Tired"
                                    onChange={this.handleChange}
                                    value="TIRED"
                                    styleColor="brown"
                                />
                                <Checkbox
                                    text="Hopeful"
                                    onChange={this.handleChange}
                                    value="HOPEFUL"
                                    styleColor="olive"
                                />
                                <Checkbox
                                    text="Irritated"
                                    onChange={this.handleChange}
                                    value="IRRITATED"
                                    styleColor="peru"
                                />
                                <Checkbox
                                    text="Surprised"
                                    onChange={this.handleChange}
                                    value="SURPRISED"
                                    styleColor="sandybrown"
                                />
                                <Checkbox
                                    text="Scared"
                                    onChange={this.handleChange}
                                    value="SCARED"
                                    styleColor="dimgrey"
                                />
                                <Checkbox
                                    text="Jealous"
                                    onChange={this.handleChange}
                                    value="JEALOUS"
                                    styleColor="darkorange"
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
                {
                    this.state.userLogged &&
                    <WeeklyStatsComponent weeklyStats={this.state.weeklyStats}/>
                }
            </div>
        )
            ;
    }
}

export default StateComponent;