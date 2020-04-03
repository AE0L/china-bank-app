function createDepositForm() {
    return `
      <form class=form id=deposit-form>
        <div class="form__content-wrapper">
          <h3 class="form__section-title">Transaction Details</h3>
          <input type=hidden name=type value="deposit">
          <label class="form__label">Customer Name</label>
          <div class="col-3 col-05-gap">
            <div class=text-underlabel>
              <input type=text class=form__input-text name=customer-name-last required>
              <label class=form__label-under>Last</label>
            </div>
            <div class=text-underlabel>
              <input type=text class=form__input-text name=customer-name-first required>
              <label class=form__label-under>First</label>
            </div>
            <div class=text-underlabel>
              <input type=text class=form__input-text name=customer-name-middle required>
              <label class=form__label-under>Middle</label>
            </div>
          </div>
          <label class="form__label">Account Number</label>
          <input class="form__input-text monospace" type=text name=account-no required>
          <label class="form__label">Currency</label>
          <div class=form__radio-group>
            <div id=php-curr>
              <input id=curr-php class=form__input-radio checked name=currency type=radio value=PHP>
              <label for=curr-php class=form__label-radio>PHP</label>
            </div>
            <div id=usd-curr>
              <input id=curr-usd class=form__input-radio name=currency type=radio value=USD>
              <label class=form__label-radio for=curr-usd>USD</label>
            </div>
            <div id=other-curr>
              <input class=form__input-radio id=other-curr-radio name=currency type=radio>
              <input class="form__input-text form__label-radio" size=10 id=other-curr-input placeholder="Other" type=text disabled required>
            </div>
          </div>
          <h3 class=form__section-title>Cash Breakdown</h3>
          <table class=form__table id=cash-breakdown><tbody></tbody></table>
          <input id=input-total-amount type=hidden name=total-amount value="0.00">
          <div class="total-amount">
            <label>Total Amount:</label>
            <label id=total-amount-curr>PHP</label> <label id=total-amount>0.00</label>
            <button class="form__button form__button-primary">Save Transaction</button>
          </div>
        </div>
      </form>

      <div id=modal class="custom-scrollbar modal hidden">
        <div class=modal__content-verify>
          <h2 class=form__section-title>Transaction Confirmation</h2>
          <p class=modal__verify-text>Please confirm the following details.</p>

          <label class=form__label>Transaction Type</label>
          <label>Cash Deposit</label>

          <label class=form__label>Account Name</label>
          <label id=ver-account-no></label>

          <label class=form__label>Currency</label>
          <label id=ver-currency></label>

          <h3 class=form__section-title>Cash Breakdown</h3>
          <table class=form__table>
            <tbody id=ver-table-header></tbody>
            <tbody id=ver-table-body></tbody>
          </table>

          <h3 class=form__section-title>Total Amount</h3>
          <div><!-- SPACE --></div>
          <label id=ver-total-amount></label>

          <div class=modal__button-wrapper>
            <button  id=ver-cancel class=modal__button>Cancel</button>
            <button  id=ver-submit class=modal__button>Submit</button>
          </div>
        </div>
      </div>
  `
}
