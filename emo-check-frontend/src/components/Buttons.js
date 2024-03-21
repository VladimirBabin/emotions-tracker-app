import * as React from "react";
import classNames from "classnames";
import './Buttons.css'

export default function Buttons(props) {
    return (
        <div>
            <button className={classNames("btn btn-primary", "btn-login")} onClick={props.login}>Login</button>
            <button className={classNames("btn btn-dark", "btn-logout")} onClick={props.logout}>Logout</button>
        </div>

    );
};