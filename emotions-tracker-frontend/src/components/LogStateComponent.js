import * as React from "react";
import EntriesApiClient from "../services/EntriesApiClient";
import './LogStateComponent.css'
import {getUserId} from "../services/AuthApiClient";
import FormDateTimeView from "../helper_components/FormDateTimeView";
import EmotionCheckboxes from "../helper_components/EmotionCheckboxes";
import StateRadioButtons from "../helper_components/StateRadioButtons";

class LogStateComponent extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            state: '',
            emotions: [],
            comment: '',
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
            this.props.date.format('YYYY-MM-DDTHH:mm:ss').toString();
        EntriesApiClient.sendState(getUserId(),
            this.state.state,
            this.state.emotions,
            this.state.comment,
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
                            <StateRadioButtons handleRadiobutton={this.handleSetChange}/>
                        </div>
                        <div className="state-div">
                            <div className="state-title">
                                <h6>{this.props.date === null ? "How do you feel?" : "How did you feel?"}</h6>
                            </div>
                            <EmotionCheckboxes handleCheckbox={this.handleCheckbox}/>
                        </div>
                        <div className="state-div">
                            <div className="state-title">
                                <h6>Quick note:</h6>
                            </div>
                            <input className="comment-input" type="text" name="comment" placeholder="Add note..."
                                   onChange={this.handleSetChange}/>
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