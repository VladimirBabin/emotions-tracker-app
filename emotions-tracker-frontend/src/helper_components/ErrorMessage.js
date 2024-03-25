import * as React from "react";

export default class ErrorMessage extends React.Component<{ message: any }> {
    render() {
        return (
            <div className="row justify-content-md-center" style={{marginTop: "2%"}}>
                <div className="jumbotron jumbotron-fluid">
                    <div className="container">
                        <p className="lead" style={{color: "indianred", fontWeight: "bold"}}>{this.props.message}</p>
                    </div>
                </div>
            </div>
        );
    }
}