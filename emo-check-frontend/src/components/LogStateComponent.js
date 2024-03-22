import * as React from "react";
import ApiClient from "../services/ApiClient";
import RadioButton from "../helper_components/RadioButton";
import Checkbox from "../helper_components/Checkbox"
import './LogStateComponent.css'

class LogStateComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            user: '',
            state: '',
            dateTime: '',
            userLogged: false,
            emotions: [],
            message: ''
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
                                <h6>How do you feel?</h6>
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