import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';
import '../assets/styles/th-bank.css';

const HeaderAct = (props) => {
    const { deleteUserData } = props;
    const navigate = useNavigate();

    const handleCancel = () => {
        deleteUserData();
        navigate('/');
    };

    return (
        <header className="header">
            <button type="button" className="header__lft" onClick={() => navigate(-1)}>
                <span>&lt; Back</span>
            </button>
            <button type="button" className="header__rgt header__cxl" onClick={handleCancel}>
                <span className="blind">Cancel</span>
            </button>
        </header>
    );
};

const mapStateToProps = () => ({

});

const mapDispatchToProps = (dispatch) => ({
    async deleteUserData() {
        dispatch({ type: 'USER_LOGOUT' });
    },
});

export default connect(mapStateToProps, mapDispatchToProps)(HeaderAct);
