import './App.css'
import AppContent from "./AppContent";
import * as React from "react";
import Buttons from "./Buttons";
import WelcomeContent from "./WelcomeContent";
import LoginForm from "./LoginForm";
import {request, setAuthToken, setLogin, setUserId} from "../axios_helper";

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            componentToShow: "welcome"
        };
    };

    login = () => {
        this.setState({componentToShow: "login"});
    };

    logout = () => {
        this.setState({componentToShow: "welcome"});
    };

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
        });
    };

    render () {
        return (
            <div>
                <header className="app-header">
                    <h1 className="app-title" >
                        Emotions Tracker
                        <Buttons login={this.login} logout={this.logout}/>
                    </h1>
                </header>

                <div className="container-fluid" style={{marginTop: "3%"}}>
                    <div className="row">
                        <div className="col">
                            {this.state.componentToShow === "welcome" && <WelcomeContent/>}
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