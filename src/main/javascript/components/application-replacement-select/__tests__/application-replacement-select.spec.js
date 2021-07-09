import { initApplicationReplacementSelect } from "../application-replacement-select";
import * as http from "../../../js/fetch";

jest.mock("../../../js/fetch");

describe("application-replacement-select", function () {
  let selectElement;
  let submitButtonElement;

  beforeEach(async function () {
    document.body.innerHTML = `
      <form>
        <div>
          <div>
            <select id="holiday-replacement-select">
              <option value="1">batman</option>
              <option value="2">robin</option>
              <option value="3">joker</option>
              <option value="4">pinguin</option>
            </select>
          </div>
          <button formaction="/my-formaction">submit</button>
        </div>
        <div id="replacement-section-container">
          <ul></ul>
        </div>
      </form>
    `;

    selectElement = document.querySelector("select");
    submitButtonElement = document.querySelector("button");

    initApplicationReplacementSelect();

    jest.useFakeTimers();
  });

  afterEach(function () {
    jest.clearAllTimers();

    while (document.body.firstChild) {
      document.body.firstChild.remove();
    }

    jest.clearAllMocks();
  });

  it("adds initially hidden loading indicator to the submit button", function () {
    expect(submitButtonElement.querySelectorAll("svg")).toHaveLength(1);
  });

  it("shows loading indicator when 'select' value has changed", function () {
    expect(submitButtonElement.querySelector("svg").classList.contains("tw-w-4")).toBeFalsy();
    expect(submitButtonElement.querySelector("svg").classList.contains("tw-mr-2")).toBeFalsy();

    selectElement.value = "3";
    selectElement.dispatchEvent(new Event("change"));

    expect(submitButtonElement.querySelector("svg").classList.contains("tw-w-4")).toBeTruthy();
    expect(submitButtonElement.querySelector("svg").classList.contains("tw-mr-2")).toBeTruthy();
  });

  it("hides loading indicator when POST response is 'ok'", async function () {
    http.post.mockReturnValue(
      Promise.resolve({
        ok: true,
        text: () => Promise.resolve("<li>my response</li>"),
      }),
    );

    selectElement.value = "3";
    selectElement.dispatchEvent(new Event("change"));

    expect(submitButtonElement.querySelector("svg").classList.contains("tw-w-4")).toBeTruthy();
    expect(submitButtonElement.querySelector("svg").classList.contains("tw-mr-2")).toBeTruthy();

    jest.advanceTimersToNextTimer();
    // await allSettled POST
    await Promise.resolve();
    await Promise.resolve();
    // await response.text
    await Promise.resolve();

    expect(submitButtonElement.querySelector("svg").classList.contains("tw-w-4")).toBeFalsy();
    expect(submitButtonElement.querySelector("svg").classList.contains("tw-mr-2")).toBeFalsy();
  });

  it("hides loading indicator when POST response is 'not ok'", async function () {
    http.post.mockReturnValue(
      Promise.resolve({
        ok: false,
      }),
    );

    selectElement.value = "3";
    selectElement.dispatchEvent(new Event("change"));

    expect(submitButtonElement.querySelector("svg").classList.contains("tw-w-4")).toBeTruthy();
    expect(submitButtonElement.querySelector("svg").classList.contains("tw-mr-2")).toBeTruthy();

    jest.advanceTimersToNextTimer();
    // await allSettled POST
    await Promise.resolve();
    await Promise.resolve();
    // await response.text
    await Promise.resolve();

    expect(submitButtonElement.querySelector("svg").classList.contains("tw-w-4")).toBeFalsy();
    expect(submitButtonElement.querySelector("svg").classList.contains("tw-mr-2")).toBeFalsy();
  });

  it("removes selected person from 'select' when response is 'ok'", async function () {
    http.post.mockReturnValue(
      Promise.resolve({
        ok: true,
        text: () => Promise.resolve("<li>my response</li>"),
      }),
    );

    selectElement.value = "3";
    selectElement.dispatchEvent(new Event("change"));

    jest.advanceTimersToNextTimer();
    // await allSettled POST
    await Promise.resolve();
    await Promise.resolve();

    expect(selectElement.querySelector("option[value='3']")).toBeTruthy();

    // await response.text
    await Promise.resolve();

    expect(selectElement.querySelector("option[value='3']")).toBeFalsy();
  });

  it("renders fetched html for the selected person", async function () {
    http.post.mockReturnValue(
      Promise.resolve({
        ok: true,
        text: () => Promise.resolve("<li>awesome response</li>"),
      }),
    );

    selectElement.value = "3";
    selectElement.dispatchEvent(new Event("change"));

    jest.advanceTimersToNextTimer();
    // await allSettled POST
    await Promise.resolve();
    await Promise.resolve();

    expect(document.body.querySelectorAll("#replacement-section-container ul li")).toHaveLength(0);

    // await response.text
    await Promise.resolve();

    expect(document.body.querySelector("#replacement-section-container ul").innerHTML).toBe(
      "<li>awesome response</li>",
    );
  });

  it("delays response render to have a smooth transition", async function () {
    http.post.mockReturnValue(
      Promise.resolve({
        ok: true,
        text: () => Promise.resolve("<li>awesome response</li>"),
      }),
    );

    selectElement.value = "3";
    selectElement.dispatchEvent(new Event("change"));

    jest.advanceTimersByTime(299);
    // await allSettled POST
    await Promise.resolve();
    await Promise.resolve();
    // await response.text
    await Promise.resolve();

    expect(document.body.querySelectorAll("#replacement-section-container ul li")).toHaveLength(0);

    jest.advanceTimersToNextTimer();

    // await allSettled POST
    await Promise.resolve();
    await Promise.resolve();
    // await response.text
    await Promise.resolve();

    expect(document.body.querySelectorAll("#replacement-section-container ul li")).toHaveLength(1);
  });
});
