import React, { useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';
import '../assets/styles/th-bank.css';

import HeaderAct from '../components/HeaderAct';
import CommonUtils from '../utils/CommonUtils';

const TotalBalance = ({ accounts }) => {
    const totalAmount  = useMemo(() => {
        if (!accounts || accounts.length === 0) {
            return 0
        }

        const sum = accounts.reduce((acc, cur) => acc + CommonUtils.convertCurrencyRate((Number(cur.amount) || 0), cur.currency), 0);
        return sum
    }, [accounts]);

    return (
        <div className="wrap">
            <HeaderAct />

            <main className="container container--main">
                <div className="content_wrap">
                <div className="main-top" style={{ marginBottom: '16px' }}>
                    <h1 className="main-top__tit">Total Balance</h1>
                    <h2 style={{ fontSize: '28px', marginTop: '8px' }}>
                    {CommonUtils.currencySign('THB')} {totalAmount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                    </h2>
                </div>

                {accounts && accounts.length > 0 ? (
                    <div>
                    {accounts.map((acc) => (
                        <div key={acc.accountId} className="main-acc is-small" style={{background: acc.color || '', color: CommonUtils.getContrastYIQ(acc.color || '#ffffff')}}>
                        <div className="main-acc__top">
                            <h2 className="main-acc__name">{acc.nickname || 'Saving Account'}</h2>
                            <span className="main-acc__amount">{CommonUtils.currencySign(acc.currency)} {(Number(acc.amount) || 0).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })} {acc.currency !== "THB" && <span><br/>&asymp; {CommonUtils.currencySign('THB')} {CommonUtils.convertCurrencyRate((Number(acc.amount) || 0), acc.currency).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>}</span>
                            {acc.flags && acc.flags.length > 0 && acc.flags.map((flag) => <span key={flag.flagValue} className="main-acc__flag">{flag.flagValue}</span>)}
                        </div>
                        <div className="main-acc__bottom">
                            <span className="main-acc__detail">{`${acc.type || ''} ${acc.accountNumber || ''}`}</span>
                            {acc.issuer && <span className="main-acc__detail">{`Powered by ${acc.issuer}`}</span>}
                        </div>
                        </div>
                    ))}
                    </div>
                ) : (
                    <p style={{ padding: '16px' }}>No accounts available.</p>
                )}
                </div>
            </main>
        </div>
    );
};

const mapStateToProps = (state) => ({
    accounts: state.account.accounts || [],
});

export default connect(mapStateToProps)(TotalBalance);
