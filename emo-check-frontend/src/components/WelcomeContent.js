import * as React from 'react'

export default class WelcomeContent extends React.Component {
    render() {
        return (
            <div className="row justify-content-md-center">
                <div className="jumbotron jumbotron-fluid">
                    <div className="container">
                        <h1 className="display-4">Welcome to emotions tracker</h1>
                        <p className="lead">Login or register to access the app</p>
                    </div>
                </div>
            </div>
        )
    }
}