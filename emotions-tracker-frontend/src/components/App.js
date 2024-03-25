import './App.css'
import AppContent from "./AppContent";
import * as React from "react";
import Buttons from "./Buttons";
import WelcomeContent from "./WelcomeContent";
import LoginForm from "./LoginForm";
import {request, setAuthToken, setLogin, setUserId} from "../axios_helper";
import ErrorMessage from "../helper_components/ErrorMessage";


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            componentToShow: "welcome",
            message: ''
        };
    };

    login = () => {
        this.updateMessage('');
        this.setState({componentToShow: "login"});
    };

    logout = () => {
        this.updateMessage('');
        this.setState({componentToShow: "welcome"});
    };

    updateMessage(m: string) {
        this.setState({
            message: m
        });
    }

    onLogin = (e, username, password) => {
        e.preventDefault();
        window.localStorage.clear();
        request("POST",
            "/login",
            {
                login: username,
                password: password
            }
        ).then((response) => {
            this.setState({componentToShow: "appContent"});
            setAuthToken(response.data.token);
            setLogin(response.data.login);
            setUserId(response.data.id);
        }).catch((reason) => {
            this.setState({componentToShow: "welcome"});
            console.log('Login error', reason);
            this.updateMessage("Error: " + (reason.response.data.message !== null ?
                reason.response.data.message :
                "Server is not available. " + reason));
        });
    };

    onRegister = (e, firstName, lastName, username, password) => {
        e.preventDefault();
        window.localStorage.clear();
        request("POST",
            "/register",
            {
                firstName: firstName,
                lastName: lastName,
                login: username,
                password: password
            }
        ).then((response) => {
            this.setState({componentToShow: "appContent"});
            setAuthToken(response.data.token);
            setLogin(response.data.login);
            setUserId(response.data.id);
        }).catch((reason) => {
            this.setState({componentToShow: "welcome"});
            console.log('Register error', reason);
            this.updateMessage("Error: " + (reason.response.data.message !== null ?
                reason.response.data.message :
                "Server is not available. " + reason));
        });
    };

    render() {
        return (
            <div>
                <header className="app-header">
                    <h1 className="app-title">
                        Emotions Tracker
                        <Buttons login={this.login} logout={this.logout}/>
                    </h1>
                </header>

                <div className="container-fluid" style={{marginTop: "3%"}}>
                    <div className="row">
                        <div className="col">
                            {this.state.componentToShow === "welcome" && <WelcomeContent/>}
                                <ErrorMessage message={this.state.message}/>
                            {this.state.componentToShow === "appContent" && <AppContent/>}
                            {this.state.componentToShow === "login"
                                && <LoginForm onLogin={this.onLogin} onRegister={this.onRegister}/>}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default App;