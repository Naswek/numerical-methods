import React from "react";

const dimensions = Array.from({ length: 19 }, (_, index) => index + 2);

function parseNum(value) {
  return Number(String(value).trim().replace(",", "."));
}

function isValidNum(value) {
  const normalized = String(value).trim().replace(",", ".");
  return normalized !== "" && normalized !== "-" && normalized !== "." && Number.isFinite(Number(normalized));
}

function inputStyle(isValid, width = "76px") {
  return {
    width,
    boxSizing: "border-box",
    border: isValid ? "1px solid #ccc" : "2px solid #ff4d4f",
    backgroundColor: isValid ? "#fff" : "#fff1f0",
    outline: "none",
    padding: "6px 8px",
    borderRadius: "4px",
    textAlign: "right"
  };
}

export default function MatrixForm({
  samples,
  dimension,
  setDimension,
  matrix,
  setMatrix,
  initialApproximation,
  setInitialApproximation,
  epsilon,
  setEpsilon,
  maxIterations,
  setMaxIterations,
  onSubmit,
  loading,
  onUseExample,
  onGenerate
}) {
  const methodList = samples.methods?.matrix?.length
    ? samples.methods.matrix
    : ["Метод простых итераций (Якоби)"];
  const isMatrixValid = matrix.every((row) => row.every(isValidNum));
  const isInitialValid =
    initialApproximation.length === dimension && initialApproximation.every(isValidNum);
  const isEpsilonValid = isValidNum(epsilon) && parseNum(epsilon) > 0 && parseNum(epsilon) < 1;
  const iterationsNumber = Number(maxIterations);
  const isIterationsValid =
    Number.isInteger(iterationsNumber) && iterationsNumber > 0 && iterationsNumber <= 100000;
  const isFormValid = isMatrixValid && isInitialValid && isEpsilonValid && isIterationsValid;

  function updateCell(rowIndex, columnIndex, value) {
    setMatrix((current) =>
      current.map((row, r) => row.map((cell, c) => (r === rowIndex && c === columnIndex ? value : cell)))
    );
  }

  function updateInitial(index, value) {
    setInitialApproximation((current) =>
      current.map((cell, currentIndex) => (currentIndex === index ? value : cell))
    );
  }

  return (
    <form
      onSubmit={onSubmit}
      style={{
        padding: 20,
        border: "1px solid #ddd",
        borderRadius: 8,
        display: "grid",
        gap: 14,
        maxWidth: 860,
        margin: "0 auto 20px",
        textAlign: "left"
      }}
    >
      <h3 style={{ margin: 0 }}>Настройки СЛАУ</h3>

      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))", gap: 12 }}>
        <div>
          <label style={{ fontSize: "12px", color: "gray" }}>Метод</label>
          <select value={0} disabled style={{ width: "100%", padding: "6px 8px", boxSizing: "border-box" }}>
            {methodList.map((method, index) => (
              <option key={method} value={index}>
                {method}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label style={{ fontSize: "12px", color: "gray" }}>Размерность</label>
          <select
            value={dimension}
            onChange={(event) => setDimension(Number(event.target.value))}
            style={{ width: "100%", padding: "6px 8px", boxSizing: "border-box" }}
          >
            {dimensions.map((value) => (
              <option key={value} value={value}>
                {value}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label style={{ fontSize: "12px", color: "gray" }}>Точность epsilon</label>
          <input
            value={epsilon}
            onChange={(event) => setEpsilon(event.target.value)}
            style={inputStyle(isEpsilonValid, "100%")}
            placeholder="0.001"
          />
        </div>

        <div>
          <label style={{ fontSize: "12px", color: "gray" }}>Лимит итераций</label>
          <input
            value={maxIterations}
            onChange={(event) => setMaxIterations(event.target.value)}
            style={inputStyle(isIterationsValid, "100%")}
            inputMode="numeric"
            placeholder="1000"
          />
        </div>
      </div>

      <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
        <button type="button" onClick={onUseExample}>
          Пример
        </button>
        <button type="button" onClick={onGenerate}>
          Сгенерировать
        </button>
      </div>

      <div style={{ overflowX: "auto", paddingBottom: 4 }}>
        <table style={{ borderCollapse: "separate", borderSpacing: 6, margin: "0 auto" }}>
          <thead>
            <tr>
              {Array.from({ length: dimension }, (_, index) => (
                <th key={`x${index}`} style={{ fontSize: "12px", color: "gray", textAlign: "center" }}>
                  x{index + 1}
                </th>
              ))}
              <th style={{ fontSize: "12px", color: "gray", textAlign: "center" }}>b</th>
            </tr>
          </thead>
          <tbody>
            {matrix.map((row, rowIndex) => (
              <tr key={rowIndex}>
                {row.map((cell, columnIndex) => (
                  <td key={`${rowIndex}-${columnIndex}`}>
                    <input
                      value={cell}
                      onChange={(event) => updateCell(rowIndex, columnIndex, event.target.value)}
                      style={inputStyle(isValidNum(cell))}
                    />
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div>
        <label style={{ fontSize: "12px", color: "gray" }}>Начальное приближение</label>
        <div style={{ display: "flex", gap: 6, flexWrap: "wrap", marginTop: 6 }}>
          {initialApproximation.map((cell, index) => (
            <label key={index} style={{ display: "grid", gap: 4, fontSize: "12px", color: "gray" }}>
              x{index + 1}
              <input
                value={cell}
                onChange={(event) => updateInitial(index, event.target.value)}
                style={inputStyle(isValidNum(cell))}
              />
            </label>
          ))}
        </div>
      </div>

      <button type="submit" disabled={loading || !isFormValid} style={{ padding: "8px", cursor: "pointer" }}>
        {loading ? "Решаем..." : "Решить систему"}
      </button>
    </form>
  );
}
