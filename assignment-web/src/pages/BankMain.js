import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';
import '../assets/styles/th-bank.css';
import Dialog from '../components/Dialog';
import alertUtils from '../utils/AlertUtils';

const BankMain = (props) => {
    const { user, deleteUserData } = props;
    const navigate = useNavigate();
    const [dialogData, setDialogData] = useState(alertUtils.close());

    useEffect(() => {
        if (!user || !user.userId) {
            setDialogData(
                alertUtils.alert({
                    title: 'ระบบแจ้งเตือน',
                    message: 'กรุณาเข้าสู่ระบบอีกครั้ง',
                    onClose: () => {
                        deleteUserData();
                        navigate('/login');
                    },
                }),
            );
            return;
        }

        setDialogData(alertUtils.close());
    }, [user, navigate, deleteUserData]);

    const handleCancel = () => {
        deleteUserData();
        navigate('/');
    };

    const handleDialogClose = (callback) => {
        setDialogData(alertUtils.close());
        if (callback) {
            callback();
        }
    };

    return (
        <div className="wrap">
        <header className="header">
            <a href="#" className="header__lft header__menu" onClick={(event) => event.preventDefault()}>
            <span className="blind">Menu</span>
            </a>
            <button type="button" className="header__rgt header__cxl" onClick={handleCancel}>
            <span className="blind">Cancel</span>
            </button>
        </header>

        <main className="container container--main">
            <div className="content_wrap">
            <div className="main-top">
                <h1 className="main-top__tit main-loading main-loading--order1">{user ? user.greetingText || `Have a nice day ${user.name || ''}` : 'Have a nice day'}</h1>
            </div>

            <div className="main-acc main-acc--large main-loading main-loading--order3">
                <div className="main-acc__top">
                <h2 className="main-acc__name">Saving Account</h2>
                <span className="main-acc__amount">฿62,000.00</span>
                <span className="main-acc__detail main-acc__detail--num">Smart account 568-2-81740-9</span>
                <span className="main-acc__detail">Powered by TestLab</span>
                </div>

                <div className="main-acc__bottom">
                <div className="main-acc__ani_box">
                    <div className="main-acc__ani__item">
                    <span className="main-acc__ani_img1" />
                    <span className="main-acc__ani_img2" />
                    </div>
                    <div className="main-acc__ani__item2">
                    <span className="main-acc__ani_img3" />
                    </div>
                </div>
                <div className="main-acc__link__box">
                    <div className="main-acc__link__item">
                    <a href="#" className="main-acc__link main-acc__link--withdrawal" onClick={(event) => event.preventDefault()}>
                        Withdrawal
                    </a>
                    <a href="#" className="main-acc__link main-acc__link--qr" onClick={(event) => event.preventDefault()}>
                        QR scan
                    </a>
                    <a href="#" className="main-acc__link main-acc__link--addmoney" onClick={(event) => event.preventDefault()}>
                        Add money
                    </a>
                    </div>
                </div>
                </div>
                <button type="button" className="main-acc__more">
                <span className="blind">More Action</span>
                </button>
                <div className="tooltip" style={{ display: 'none' }}>
                <button type="button" className="tooltip__btn-more">
                    Set main account
                </button>
                <button type="button" className="tooltip__btn-more">
                    Copy account number
                </button>
                <button type="button" className="tooltip__btn-more">
                    Edit Name and Color
                </button>
                </div>
                <div className="tooltip tooltip--bubble tooltip--right-under" style={{ display: 'none' }}>
                <span className="tooltip__txt">
                    Change your main account for
                    <br />
                    Using transfer, Wallet more easliy
                </span>
                </div>
            </div>

            <div className="rctly__wrap main-loading main-loading--order5">
                <ul className="rctly__lst">
                <li className="rctly__item">
                    <a href="#" className="rctly__link" onClick={(event) => event.preventDefault()}>
                    <span className="rctly__thumb">
                        <img src="https://dummyimage.com/54x54/999/fff" alt="" />
                    </span>
                    <span className="rctly__name">Emily</span>
                    </a>
                </li>
                <li className="rctly__item">
                    <a href="#" className="rctly__link" onClick={(event) => event.preventDefault()}>
                    <span className="rctly__thumb is-bank">
                        <img src="https://dummyimage.com/54x54/999/fff" alt="" />
                    </span>
                    <span className="rctly__name">AbcdEfghiJKlmN</span>
                    </a>
                </li>
                <li className="rctly__item">
                    <a href="#" className="rctly__link" onClick={(event) => event.preventDefault()}>
                    <span className="rctly__thumb">
                        <img src="https://dummyimage.com/54x54/999/fff" alt="" />
                    </span>
                    <span className="rctly__name">Jone Kiersten</span>
                    </a>
                </li>
                <li className="rctly__item">
                    <a href="#" className="rctly__link" onClick={(event) => event.preventDefault()}>
                    <span className="rctly__thumb">
                        <img src="https://dummyimage.com/54x54/999/fff" alt="" />
                    </span>
                    <span className="rctly__name">Emily</span>
                    </a>
                </li>
                <li className="rctly__item">
                    <a href="#" className="rctly__link" onClick={(event) => event.preventDefault()}>
                    <span className="rctly__thumb">
                        <img src="https://dummyimage.com/54x54/999/fff" alt="" />
                    </span>
                    <span className="rctly__name">Emily</span>
                    </a>
                </li>
                <li className="rctly__item">
                    <a href="#" className="rctly__link" onClick={(event) => event.preventDefault()}>
                    <span className="rctly__thumb is-bank">
                        <img src="https://dummyimage.com/54x54/999/fff" alt="" />
                    </span>
                    <span className="rctly__name">MarkYu Gonzales</span>
                    </a>
                </li>
                </ul>
            </div>

            <a className="main-make main-loading main-loading--order6" style={{ display: 'none' }}>
                <span className="main-make__img">
                <img src="./assets/img-debitcard-make.png" alt="" />
                </span>
                <strong className="main-make__tit">Make your Debit Card</strong>
                <p className="main-make__dsc">To enjoy 0.5% cash back from online perchase.</p>
            </a>

            <div className="debit-swipe__wrap main-loading main-loading--order6">
                <div className="debit-swipe__inner">
                <div className="debit-swipe__lst" style={{ width: '1595px' }}>
                    <a
                    href="#"
                    className="debit-swipe__item"
                    style={{ backgroundColor: '#00a1e2' }}
                    onClick={(event) => event.preventDefault()}
                    >
                    <strong className="debit-swipe__name">My Salary</strong>
                    <span className="debit-swipe__etc">In progress</span>
                    <span className="debit-swipe__issue">Issued by TestLab</span>
                    </a>
                    <a
                    href="#"
                    className="debit-swipe__item"
                    style={{ backgroundColor: '#ff8300' }}
                    onClick={(event) => event.preventDefault()}
                    >
                    <strong className="debit-swipe__name">For My Dream</strong>
                    <span className="debit-swipe__etc">In progress</span>
                    <span className="debit-swipe__issue">Issued by TestLab</span>
                    </a>
                    <a
                    href="#"
                    className="debit-swipe__item"
                    style={{ color: '#97999e', backgroundColor: '#ffffff', '--border-color': '#f2f3f7' }}
                    onClick={(event) => event.preventDefault()}
                    >
                    <strong className="debit-swipe__name">For My Dream</strong>
                    <span className="debit-swipe__etc debit-swipe__etc--active">
                        <span className="debit-swipe__etc__num">9440 78&#8226;&#8226; &#8226;&#8226;&#8226;&#8226; 3115</span>
                    </span>
                    <span className="debit-swipe__issue" style={{ color: '#d3d3d2' }}>Issued by TestLab</span>
                    </a>
                    <a
                    href="#"
                    className="debit-swipe__item"
                    style={{ backgroundColor: '#91c9ee' }}
                    onClick={(event) => event.preventDefault()}
                    >
                    <strong className="debit-swipe__name">For My Dream</strong>
                    <span className="debit-swipe__etc debit-swipe__etc--active">
                        <span className="debit-swipe__etc__num">9440 78&#8226;&#8226; &#8226;&#8226;&#8226;&#8226; 3115</span>
                    </span>
                    <span className="debit-swipe__issue">Issued by TestLab</span>
                    </a>

                    <a href="#" className="debit-swipe__item debit-swipe__item--all" onClick={(event) => event.preventDefault()}>
                    See all
                    </a>
                </div>
                </div>
            </div>

            <div className="main-acc is-bluegreen is-small">
                <div className="main-acc__top">
                <h2 className="main-acc__name">Saving Account</h2>
                <span className="main-acc__amount">฿8,837,999.00</span>
                </div>
                <div className="main-acc__bottom">
                <span className="main-acc__detail">Smart account 568-2-81740-9</span>
                <span className="main-acc__detail">Powered by TestLab</span>
                </div>
                <button type="button" className="main-acc__more main-acc__more--small">
                <span className="blind">More Action</span>
                </button>
                <div className="tooltip tooltip--sub-acc">
                <button type="button" className="tooltip__btn-more">Copy account number</button>
                <button type="button" className="tooltip__btn-more">Edit Name and Color</button>
                </div>
                <a href="#" className="main-acc__act main-acc__act--money" onClick={(event) => event.preventDefault()}>
                <span className="blind">Add money</span>
                </a>
            </div>

            <div className="main-acc is-orange is-small">
                <div className="main-acc__top">
                <h2 className="main-acc__name">Credit Loan</h2>
                <span className="main-acc__amount">฿300.100</span>
                </div>
                <div className="main-acc__bottom">
                <span className="main-acc__detail">Credit Loan 568-2-81740-9</span>
                </div>
                <button type="button" className="main-acc__more main-acc__more--small">
                <span className="blind">More Action</span>
                </button>
                <div className="tooltip tooltip--sub-acc" style={{ display: 'none' }}>
                <button type="button" className="tooltip__btn-more">Copy account number</button>
                <button type="button" className="tooltip__btn-more">Edit Name and Color</button>
                </div>
                <a href="#" className="main-acc__act main-acc__act--disburse" onClick={(event) => event.preventDefault()}>
                <span className="blind">Disburse</span>
                </a>
            </div>

            <div className="main-acc is-purple is-small">
                <div className="main-acc__top">
                <h2 className="main-acc__name">Travel New York</h2>
                <span className="main-acc__amount">฿30,000.00</span>
                </div>
                <div className="main-acc__bottom">
                <span className="main-acc__detail">Goal driven savings 568-2-81740-9</span>
                <span className="main-acc__detail">Powered by TestLab</span>
                </div>
                <button type="button" className="main-acc__more main-acc__more--small">
                <span className="blind">More Action</span>
                </button>
                <div className="tooltip tooltip--sub-acc" style={{ display: 'none' }}>
                <button type="button" className="tooltip__btn-more">Copy account number</button>
                <button type="button" className="tooltip__btn-more">Edit Name and Color</button>
                </div>
                <div className="main-acc__circle">
                <svg className="graph-bar" width="100%" height="100%" viewBox="0 0 42 42">
                    <circle cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="rgba(0,0,0,0.07)" strokeWidth="1.5" />
                    <circle
                    className="gauge"
                    cx="21"
                    cy="21"
                    r="15.91549430918954"
                    fill="transparent"
                    stroke="#fff"
                    strokeWidth="1.5"
                    strokeLinecap="round"
                    strokeDashoffset="25"
                    style={{ strokeDasharray: '24 76' }}
                    />
                </svg>
                <div className="main-acc__num">
                    <span className="percent">24</span>
                    <span className="unit">%</span>
                </div>
                </div>
            </div>

            <div className="main-acc is-deepblue is-small">
                <div className="main-acc__top">
                <h2 className="main-acc__name">Need to repay</h2>
                <span className="main-acc__amount">฿30,000.00</span>
                <span className="main-acc__flag main-acc__flag--lock">Disbursement</span>
                <span className="main-acc__flag">Overdue</span>
                </div>
                <div className="main-acc__bottom">
                <span className="main-acc__detail">Credit Loan 568-2-81740-9</span>
                </div>
                <button type="button" className="main-acc__more main-acc__more--small">
                <span className="blind">More Action</span>
                </button>
                <div className="tooltip tooltip--sub-acc" style={{ display: 'none' }}>
                <button type="button" className="tooltip__btn-more">Copy account number</button>
                <button type="button" className="tooltip__btn-more">Edit Name and Color</button>
                </div>
                <a href="#" className="main-acc__act main-acc__act--pay" onClick={(event) => event.preventDefault()}>
                <span className="blind">Pay</span>
                </a>
            </div>

            <a href="#" className="main-prod" onClick={(event) => event.preventDefault()}>
                <span className="main-prod__cms-ico">
                <img src="https://dummyimage.com/54x54/999/fff" alt="" />
                </span>
                <strong className="main-prod__tit">Want some money?</strong>
                <p className="main-prod__dsc">You can start apply 'Clare'</p>
            </a>

            <div className="main-tb">
                <a href="#" className="link-to" onClick={(event) => event.preventDefault()}>
                Total Balance
                </a>
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
    async deleteUserData() {
        dispatch({ type: 'USER_LOGOUT' });
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(BankMain);
