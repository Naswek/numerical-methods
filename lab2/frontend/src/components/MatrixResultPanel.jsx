function formatNumber(value) {
  if (!Number.isFinite(value)) return "—";
  return Number(value).toLocaleString("ru-RU", {
    maximumFractionDigits: 10
  });
}

function MatrixTable({ matrix, vectorLabel }) {
  if (!Array.isArray(matrix) || matrix.length === 0) return null;

  return (
    <div style={{ overflowX: "auto", marginTop: 8 }}>
      <table style={{ borderCollapse: "separate", borderSpacing: 6, margin: "0 auto" }}>
        <tbody>
          {matrix.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((value, columnIndex) => (
                <td
                  key={`${rowIndex}-${columnIndex}`}
                  style={{
                    minWidth: 72,
                    padding: "6px 8px",
                    border: "1px solid #ddd",
                    borderRadius: 4,
                    textAlign: "right",
                    fontFamily: "var(--mono)"
                  }}
                >
                  {formatNumber(value)}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      {vectorLabel && <p style={{ color: "gray", fontSize: "13px", marginTop: 4 }}>{vectorLabel}</p>}
    </div>
  );
}

export default function MatrixResultPanel({ result, error }) {
  if (error) {
    return <p style={{ color: "red", marginTop: 12, fontWeight: "bold" }}>{error}</p>;
  }

  if (!result) return null;

  return (
    <div
      style={{
        margin: "20px auto 0",
        padding: 15,
        border: "1px solid #ccc",
        borderRadius: 8,
        maxWidth: 860,
        textAlign: "left"
      }}
    >
      <h3 style={{ marginTop: 0 }}>Результат</h3>

      {result.isError ? (
        <p style={{ color: "red", fontSize: "18px", fontWeight: "bold" }}>{result.message}</p>
      ) : (
        <>
          <p>
            <strong>Решение:</strong>{" "}
            [{result.solution.map((value) => formatNumber(value)).join("; ")}]
          </p>
          <p>
            <strong>Итерации:</strong> {result.iterations}
          </p>
          <p>
            <strong>Максимальная погрешность:</strong> {formatNumber(result.maxError)}
          </p>
          <p>
            <strong>Погрешности:</strong>{" "}
            [{result.errors.map((value) => formatNumber(value)).join("; ")}]
          </p>

          <h4>Переставленная матрица</h4>
          <MatrixTable matrix={result.reorderedMatrix} />

          <h4>Матрица C</h4>
          <MatrixTable matrix={result.c} />

          <p style={{ marginTop: 10 }}>
            <strong>Вектор d:</strong> [{result.d.map((value) => formatNumber(value)).join("; ")}]
          </p>

          <h4>Формулы итераций</h4>
          <div style={{ display: "grid", gap: 6 }}>
            {result.formulas.map((formula) => (
              <code key={formula} style={{ display: "block" }}>
                {formula}
              </code>
            ))}
          </div>
        </>
      )}

      <p style={{ color: "gray", marginTop: 10 }}>Статус: {result.message}</p>
    </div>
  );
}
