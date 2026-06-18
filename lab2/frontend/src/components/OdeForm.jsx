import React from "react";

export default function OdeForm({
  samples,
  functionId, setFunctionId,
  x0, setX0,
  y0, setY0,
  xn, setXn,
  h, setH,
  epsilon, setEpsilon,
  onSubmit,
  onFileUpload,
  loading
}) {
  const parseNum = (val) => Number(String(val).trim().replace(",", "."));
  const isValidNum = (val) => {
    const s = String(val).trim().replace(",", ".");
    return s !== "" && s !== "-" && s !== "." && !isNaN(Number(s));
  };

  const numX0 = parseNum(x0);
  const numXn = parseNum(xn);
  const numH = parseNum(h);
  const rawSteps = (numXn - numX0) / numH;
  const roundedSteps = Math.round(rawSteps);

  const isIntervalValid = isValidNum(x0) && isValidNum(xn) && numX0 < numXn;
  const isHValid = isValidNum(h) && numH > 0;
  const isEpsilonValid = isValidNum(epsilon) && parseNum(epsilon) > 0;
  const isGridValid = isIntervalValid && isHValid && Math.abs(rawSteps - roundedSteps) < 1e-9;
  const pointsCount = isGridValid ? roundedSteps + 1 : 0;
  const isPointsCountValid = pointsCount >= 2 && pointsCount <= 151;
  const isFormValid = isIntervalValid && isHValid && isEpsilonValid && isGridValid && isPointsCountValid && isValidNum(y0);

  const inputStyle = (isValid) => ({
    border: isValid ? "1px solid #ccc" : "2px solid #ff4d4f",
    backgroundColor: isValid ? "#fff" : "#fff1f0",
    outline: "none",
    padding: "8px",
    borderRadius: "6px",
    width: "100%",
    boxSizing: "border-box"
  });

  return (
    <div style={{
      padding: 20,
      border: "1px solid #ddd",
      borderRadius: 12,
      maxWidth: 600,
      backgroundColor: "#fff",
      boxShadow: "0 4px 10px rgba(0,0,0,0.05)"
    }}>
      <h3 style={{ marginTop: 0 }}>Настройки задачи Коши</h3>

      <form onSubmit={onSubmit} style={{ display: "grid", gap: 15 }}>
        <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
          <label style={{ fontSize: "12px", color: "gray" }}>Выберите ОДУ</label>
          <select
            value={functionId}
            onChange={(e) => setFunctionId(Number(e.target.value))}
            style={{ padding: "8px", borderRadius: "6px" }}
          >
            {samples.functions.map((f, i) => (
              <option key={i} value={i}>{f}</option>
            ))}
          </select>
        </div>

        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
          <div>
            <label style={{ fontSize: "12px", color: "gray" }}>Начальная точка (x0)</label>
            <input value={x0} onChange={e => setX0(e.target.value)} style={inputStyle(isValidNum(x0))} />
          </div>
          <div>
            <label style={{ fontSize: "12px", color: "gray" }}>Начальное значение (y0)</label>
            <input value={y0} onChange={e => setY0(e.target.value)} style={inputStyle(isValidNum(y0))} />
          </div>
        </div>

        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: 10 }}>
          <div>
            <label style={{ fontSize: "12px", color: "gray" }}>Правая граница (xn)</label>
            <input value={xn} onChange={e => setXn(e.target.value)} style={inputStyle(isValidNum(xn) && isIntervalValid)} />
          </div>
          <div>
            <label style={{ fontSize: "12px", color: "gray" }}>Шаг (h)</label>
            <input value={h} onChange={e => setH(e.target.value)} style={inputStyle(isHValid && isGridValid)} />
          </div>
          <div>
            <label style={{ fontSize: "12px", color: "gray" }}>Точность (epsilon)</label>
            <input value={epsilon} onChange={e => setEpsilon(e.target.value)} style={inputStyle(isEpsilonValid)} />
          </div>
        </div>

        <label style={{ cursor: "pointer", color: "#28a745", fontSize: "13px", fontWeight: "bold" }}>
          📂 Загрузить параметры (id x0 y0 xn h epsilon)
          <input type="file" accept=".txt" onChange={onFileUpload} style={{ display: "none" }} />
        </label>

        <div style={{
          textAlign: "center",
          padding: "10px",
          borderRadius: "8px",
          backgroundColor: isPointsCountValid && isGridValid ? "#f6ffed" : "#fff1f0",
          color: isPointsCountValid && isGridValid ? "#52c41a" : "#cf1322",
          fontSize: "14px",
          border: `1px solid ${isPointsCountValid && isGridValid ? "#b7eb8f" : "#ffa39e"}`
        }}>
          Итого точек: <b>{pointsCount}</b>
          {!isGridValid && " (шаг должен ровно делить интервал)"}
          {isGridValid && !isPointsCountValid && " (максимум 151 точка)"}
        </div>

        <button
          type="submit"
          disabled={loading || !isFormValid}
          style={{
            padding: "12px",
            fontWeight: "bold",
            fontSize: "16px",
            cursor: isFormValid ? "pointer" : "not-allowed",
            border: "none",
            borderRadius: "8px",
            transition: "0.2s"
          }}
        >
          {loading ? "Выполняем расчет..." : "Решить задачу Коши"}
        </button>
      </form>
    </div>
  );
}
