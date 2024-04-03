import "./CommentView.css"

const CommentView = ({comment}) => {
    return (
        <p className="comment-view-input">
            {comment}
        </p>
    );
}

export default CommentView;