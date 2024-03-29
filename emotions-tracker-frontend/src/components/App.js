import './App.css'
import AppContent from "./AppContent";
import * as React from "react";
import Buttons from "./Buttons";
import WelcomeContent from "./WelcomeContent";
import LoginForm from "./LoginForm";
import AuthApiClient, {setAuthToken, setUserId} from "../services/AuthApiClient";
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

    sendLogin(username, password): Promise {
        return AuthApiClient.login(username, password).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject(res.json());
                }
            }
        );
    }

    onLogin = (e, username, password) => {
        e.preventDefault();
        window.localStorage.clear();
        this.sendLogin(username, password).then(
            response => {
                this.setState({componentToShow: "appContent"});
                setAuthToken(response.token);
                setUserId(response.id);
            }).catch(error => error !== null ?
            error.then(e => {
                this.setState({componentToShow: "welcome"});
                this.updateMessage("Login error: " + (e.message !== '' ?
                    e.message :
                    "Authentication server is not available"));
            }) :
            this.updateMessage("Authentication server is not available")
        );
    };

    sendRegister(firstName, lastName, login, password): Promise {
        return AuthApiClient.register(firstName, lastName, login, password).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject(res.json());
                }
            }
        );
    }

    onRegister = (e, firstName, lastName, login, password) => {
        e.preventDefault();
        window.localStorage.clear();
        this.sendRegister(firstName, lastName, login, password).then(response => {
            this.setState({componentToShow: "appContent"});
            setAuthToken(response.token);
            setUserId(response.id);
        }).catch(error => error.then(e => {
            this.setState({componentToShow: "welcome"});
            this.updateMessage("Register error: " + (e.message !== '' ?
                e.message :
                "Authentication server is not available"));
        }));
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