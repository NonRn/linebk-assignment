import { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';
import '../assets/styles/th-bank.css';

import { authenticateLoginUserApi, getUserByIdApi } from '../services/UserService';
import Dialog from '../components/Dialog';
import alertUtils from '../utils/AlertUtils';
import Splash from '../components/Splash';

const Login = (props) => {
    const { fetchUserData, user } = props;
    const navigate = useNavigate();
    const [credentials, setCredentials] = useState({ userId: '', password: '' });
    const [formError, setFormError] = useState('');
    const [dialogData, setDialogData] = useState(alertUtils.close());
    const [isLoading, setIsLoading] = useState(false);

    const handleFieldChange = useCallback((field, value) => {
        setFormError('');
        setCredentials((prev) => ({ ...prev, [field]: value }));
    }, []);

    const handleSubmit = useCallback(
        (event) => {
            event.preventDefault();

            if (!credentials.userId.trim() || !credentials.password.trim()) {
                setFormError('Please enter User ID and Password.');
                return;
            }

            setIsLoading(true);
            authenticateLoginUserApi(credentials.userId, credentials.password)
            .then(async response => {
                if (response.status === 200) {
                    const user = await fetchUserData(credentials.userId)
                    if(user && user.success){
                        navigate('/bank');
                    } else {
                        throw new Error('Failed to fetch user data');
                    }
                } else {
                    setFormError('User ID or Password is incorrect.');
                    setCredentials((prev) => ({ ...prev, password: '' }));
                }
                setIsLoading(false);
            })
            .catch(error => {
                console.error('Authentication failed:', error);
                setIsLoading(false);
                setDialogData(
                    alertUtils.alert({
                        title: 'ระบบแจ้งเตือน',
                        message: 'เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้ง',
                        onClose: () => {
                            setCredentials((prev) => ({ ...prev, password: '' }));
                        },
                    }),
                );
            });
        },
        [credentials, navigate]
    );

    const handleDialogClose = (callback) => {
        setDialogData(alertUtils.close());
        if (callback) {
            callback();
        }
    };

    return (
        <div className="wrap">
        <main className="container container--pin-type">
            <div className="pin">
            <div className="pin__btm pin__btm--credentials">
                {isLoading && <Splash isBlur={true}/>}
                <section className="pin__credentials" aria-label="Login with user ID">
                <div className="pin__credentials-card">
                    <h2 className="pin__credentials-title">Sign in with ID</h2>
                    <form className="pin__credentials-form" onSubmit={handleSubmit}>
                    <label className="pin__field">
                        <span className="pin__field-label">User ID</span>
                        <input
                        type="text"
                        className="pin__input"
                        value={credentials.userId}
                        onChange={(event) => handleFieldChange('userId', event.target.value)}
                        placeholder="Enter your ID"
                        autoFocus
                        />
                    </label>
                    <label className="pin__field">
                        <span className="pin__field-label">Password</span>
                        <input
                        type="password"
                        className="pin__input"
                        value={credentials.password}
                        onChange={(event) => handleFieldChange('password', event.target.value)}
                        placeholder="Enter your password"
                        />
                    </label>
                    {formError ? <p className="pin__error">{formError}</p> : null}
                    <div className="pin__actions">
                        <button type="submit" className="pin__submit">
                        Continue
                        </button>
                    </div>
                    </form>
                </div>
                </section>
            </div>
            </div>
        </main>
        <Dialog
            isOpen={dialogData.isOpen}
            title={dialogData.title}
            message={dialogData.message}
            confirmLabel={dialogData.confirmLabel}
            buttons={dialogData.buttons}
            onClose={() => handleDialogClose(dialogData.onClose)}
        />
        </div>
    );
};

const mapStateToProps = (state) => {
    const { user } = state.user ;
    return {
        user,
    };
}

const mapDispatchToProps = (dispatch) => ({
    async fetchUserData(userId) {
        try {
            const { data } = await getUserByIdApi(userId);
            if (!data || Object.keys(data).length === 0) {
                return { success: false, message: 'ไม่พบข้อมูลผู้ใช้', };
            }

            dispatch({ type: 'USER_DATA', payload: { ...data } });
            return { success: true };
        } catch (error) {
            console.error('Failed to fetch user data:', error);
            return { success: false, message: 'พบข้อผิดพลาด' };
        }
    },
});

export default connect(mapStateToProps, mapDispatchToProps)(Login);