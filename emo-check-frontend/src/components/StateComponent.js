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
            userLogged: false,
            emotions: []
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
            this.state.emotions.pop(value);
        }
    }

    handleSubmitResult(event) {
        event.preventDefault();
        ApiClient.sendState(this.state.user,
            this.state.state,
            this.state.emotions,
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
                               onChange={this.handleSetChange}/>
                        <br/>
                        <br/>
                        <div className="state-div">
                            <div className="state-title">
                                <h6>How are you?</h6>
                            </div>
                            <div className="radiobutton-div">
                                <RadioButton
                                    text="Awful"
                                    onChange={this.handleSetChange}
                                    value="AWFUL"
                                    styleColor="indigo"
                                />
                                <RadioButton
                                    text="Bad"
                                    onChange={this.handleSetChange}
                                    value="BAD"
                                    styleColor="cornflowerblue"
                                />
                                <RadioButton
                                    text="Ok"
                                    onChange={this.handleSetChange}
                                    value="OK"
                                    styleColor="yellowgreen"
                                />
                                <RadioButton
                                    text="Good"
                                    onChange={this.handleSetChange}
                                    value="GOOD"
                                    styleColor="mediumseagreen"
                                />
                                <RadioButton
                                    text="Excellent"
                                    onChange={this.handleSetChange}
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
                                    onChange={this.handleCheckbox}
                                    value="HAPPY"
                                    styleColor="yellow"
                                />
                                <Checkbox
                                    text="Indifferent"
                                    onChange={this.handleCheckbox}
                                    value="INDIFFERENT"
                                    styleColor="skyblue"
                                />
                                <Checkbox
                                    text="Sad"
                                    onChange={this.handleCheckbox}
                                    value="SAD"
                                    styleColor="rebeccapurple"
                                />
                                <Checkbox
                                    text="Excited"
                                    onChange={this.handleCheckbox}
                                    value="EXCITED"
                                    styleColor="deeppink"
                                />
                                <Checkbox
                                    text="Peaceful"
                                    onChange={this.handleCheckbox}
                                    value="PEACEFUL"
                                    styleColor="darkolivegreen"
                                />
                                <Checkbox
                                    text="Anxious"
                                    onChange={this.handleCheckbox}
                                    value="ANXIOUS"
                                    styleColor="cyan"
                                />
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Satisfied"
                                    onChange={this.handleCheckbox}
                                    value="SATISFIED"
                                    styleColor="lightpink"
                                />
                                <Checkbox
                                    text="Content"
                                    onChange={this.handleCheckbox}
                                    value="CONTENT"
                                    styleColor="indianred"
                                />
                                <Checkbox
                                    text="Drained"
                                    onChange={this.handleCheckbox}
                                    value="DRAINED"
                                    styleColor="black"
                                />
                                <Checkbox
                                    text="Passionate"
                                    onChange={this.handleCheckbox}
                                    value="PASSIONATE"
                                    styleColor="darkorange"
                                />
                                <Checkbox
                                    text="Stressed"
                                    onChange={this.handleCheckbox}
                                    value="STRESSED"
                                    styleColor="powderblue"
                                />
                                <Checkbox
                                    text="Angry"
                                    onChange={this.handleCheckbox}
                                    value="ANGRY"
                                    styleColor="crimson"
                                />
                            </div>
                            <div className="checkbox-div">
                                <Checkbox
                                    text="Tired"
                                    onChange={this.handleCheckbox}
                                    value="TIRED"
                                    styleColor="brown"
                                />
                                <Checkbox
                                    text="Hopeful"
                                    onChange={this.handleCheckbox}
                                    value="HOPEFUL"
                                    styleColor="olive"
                                />
                                <Checkbox
                                    text="Irritated"
                                    onChange={this.handleCheckbox}
                                    value="IRRITATED"
                                    styleColor="peru"
                                />
                                <Checkbox
                                    text="Surprised"
                                    onChange={this.handleCheckbox}
                                    value="SURPRISED"
                                    styleColor="sandybrown"
                                />
                                <Checkbox
                                    text="Scared"
                                    onChange={this.handleCheckbox}
                                    value="SCARED"
                                    styleColor="dimgrey"
                                />
                                <Checkbox
                                    text="Jealous"
                                    onChange={this.handleCheckbox}
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
        );
    }
}

export default StateComponent;