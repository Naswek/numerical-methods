import React, { useState } from "react";

function formatNum(value, digits = 6) {
  return typeof value === "number" && Number.isFinite(value) ? value.toFixed(digits) : "—";
}

function MethodTable({ points }) {
  if (!points || points.length === 0) return null;

  return (
    <div style={{
      marginTop: 15,
      overflowX: "auto",
      border: "1px solid #ddd",
      padding: 12,
      borderRadius: 8,
      backgroundColor: "#fcfcfc"
    }}>
      <h5 style={{ marginTop: 0, marginBottom: 10, color: "#333" }}>Таблица значений</h5>
      <table border="1" cellPadding="6" style={{
        width: "100%",
        borderCollapse: "collapse",
        fontSize: "12px",
        textAlign: "center",
        borderColor: "#eee"
      }}>
        <thead>
          <tr style={{ backgroundColor: "#f2f2f2" }}>
            <th>i</th>
            <th>x_i</th>
            <th>y_i</th>
            <th>y точн.</th>
            <th>|y точн. - y_i|</th>
          </tr>
        </thead>
        <tbody>
          {points.map((p, i) => (
            <tr key={i}>
              <td style={{ backgroundColor: "#fafafa", fontWeight: "bold" }}>{p.i}</td>
              <td>{formatNum(p.x, 4)}</td>
              <td style={{ fontWeight: "bold" }}>{formatNum(p.y)}</td>
              <td>{formatNum(p.exactY)}</td>
              <td>{formatNum(p.error)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default function OdeResultPanel({ result, error }) {
  const [activeTableIdx, setActiveTableIdx] = useState(null);

  if (error) return <div style={{ color: "red", padding: 10, fontWeight: "bold" }}>{error}</div>;
  if (!result) return null;

  const toggleTable = (idx) => {
    setActiveTableIdx(activeTableIdx === idx ? null : idx);
  };

  return (
    <div style={{ marginTop: 20, border: "1px solid #ccc", padding: 15, borderRadius: 12, backgroundColor: "#fff" }}>
      <h3 style={{ marginTop: 0, borderBottom: "1px solid #eee", paddingBottom: 10 }}>Результаты решения ОДУ</h3>
      <p><b>Наилучший метод:</b> <span style={{ color: "green" }}>{result.bestMethod || "—"}</span></p>

      <table border="1" cellPadding="10" style={{ width: "100%", borderCollapse: "collapse", borderColor: "#ddd" }}>
        <thead>
          <tr style={{ backgroundColor: "#f2f2f2" }}>
            <th>Метод</th>
            <th>Макс. ошибка</th>
            <th>Погрешность Рунге</th>
            <th>Статус</th>
            <th>Таблица</th>
          </tr>
        </thead>
        <tbody>
          {result.results.map((res, i) => {
            const isBest = res.methodName === result.bestMethod;
            return (
              <React.Fragment key={i}>
                <tr style={isBest ? { fontWeight: "bold", background: "#f3e8ff" } : {}}>
                  <td>{res.methodName}</td>
                  <td>{formatNum(res.maxError)}</td>
                  <td>{res.rungeError > 0 ? formatNum(res.rungeError) : "—"}</td>
                  <td style={{ color: res.isSuccess ? "green" : "red", fontSize: "13px" }}>
                    {res.message}
                  </td>
                  <td>
                    <button
                      type="button"
                      onClick={() => toggleTable(i)}
                      style={{
                        padding: "4px 8px",
                        fontSize: "12px",
                        cursor: "pointer",
                        backgroundColor: activeTableIdx === i ? "#6c757d" : "#007bff",
                        color: "white",
                        border: "none",
                        borderRadius: "4px"
                      }}
                    >
                      {activeTableIdx === i ? "Скрыть" : "Показать"}
                    </button>
                  </td>
                </tr>
                {activeTableIdx === i && (
                  <tr>
                    <td colSpan="5">
                      <MethodTable points={res.points} />
                    </td>
                  </tr>
                )}
              </React.Fragment>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
